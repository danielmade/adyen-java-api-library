/*
 *                       ######
 *                       ######
 * ############    ####( ######  #####. ######  ############   ############
 * #############  #####( ######  #####. ######  #############  #############
 *        ######  #####( ######  #####. ######  #####  ######  #####  ######
 * ###### ######  #####( ######  #####. ######  #####  #####   #####  ######
 * ###### ######  #####( ######  #####. ######  #####          #####  ######
 * #############  #############  #############  #############  #####  ######
 *  ############   ############  #############   ############  #####  ######
 *                                      ######
 *                               #############
 *                               ############
 *
 * Adyen Java API Library
 *
 * Copyright (c) 2017 Adyen B.V.
 * This file is open source and available under the MIT license.
 * See the LICENSE file for more info.
 */
package com.adyen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.adyen.model.AccountHolderBalanceRequest;
import com.adyen.model.AccountHolderBalanceResponse;
import com.adyen.model.AccountHolderDetails;
import com.adyen.model.AccountHolderTransactionListRequest;
import com.adyen.model.AccountHolderTransactionListResponse;
import com.adyen.model.Address;
import com.adyen.model.Amount;
import com.adyen.model.BankAccountDetail;
import com.adyen.model.CloseAccountHolderRequest;
import com.adyen.model.CloseAccountHolderResponse;
import com.adyen.model.CloseAccountRequest;
import com.adyen.model.CloseAccountResponse;
import com.adyen.model.CreateAccountHolderRequest;
import com.adyen.model.CreateAccountHolderResponse;
import com.adyen.model.CreateAccountRequest;
import com.adyen.model.CreateAccountResponse;
import com.adyen.model.DeleteBankAccountRequest;
import com.adyen.model.DeleteBankAccountResponse;
import com.adyen.model.DeleteShareholderRequest;
import com.adyen.model.DeleteShareholderResponse;
import com.adyen.model.DocumentDetail;
import com.adyen.model.FraudCheckResult;
import com.adyen.model.GetAccountHolderRequest;
import com.adyen.model.GetAccountHolderResponse;
import com.adyen.model.GetUploadedDocumentsRequest;
import com.adyen.model.GetUploadedDocumentsResponse;
import com.adyen.model.IndividualDetails;
import com.adyen.model.Name;
import com.adyen.model.PaymentRequest;
import com.adyen.model.PaymentResult;
import com.adyen.model.PayoutAccountHolderRequest;
import com.adyen.model.PayoutAccountHolderResponse;
import com.adyen.model.PersonalData;
import com.adyen.model.PhoneNumber;
import com.adyen.model.RefundNotPaidOutTransfersRequest;
import com.adyen.model.RefundNotPaidOutTransfersResponse;
import com.adyen.model.SetupBeneficiaryRequest;
import com.adyen.model.SetupBeneficiaryResponse;
import com.adyen.model.SuspendAccountHolderRequest;
import com.adyen.model.SuspendAccountHolderResponse;
import com.adyen.model.TransactionListForAccount;
import com.adyen.model.TransferFundsRequest;
import com.adyen.model.TransferFundsResponse;
import com.adyen.model.UnSuspendAccountHolderRequest;
import com.adyen.model.UnSuspendAccountHolderResponse;
import com.adyen.model.UpdateAccountHolderRequest;
import com.adyen.model.UpdateAccountHolderResponse;
import com.adyen.model.UpdateAccountHolderStateRequest;
import com.adyen.model.UpdateAccountHolderStateResponse;
import com.adyen.model.UpdateAccountRequest;
import com.adyen.model.UpdateAccountResponse;
import com.adyen.model.UpdatePayoutScheduleRequest;
import com.adyen.model.UploadDocumentRequest;
import com.adyen.model.UploadDocumentResponse;
import com.adyen.model.additionalData.SplitPayment;
import com.adyen.model.additionalData.SplitPaymentItem;
import com.adyen.service.Account;
import com.adyen.service.Fund;
import com.adyen.service.Payment;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for /authorise and /authorise3d
 */
public class MarketPayTest extends BaseTest {
    /**
     * Test success flow for POST /authorise
     */
    @Test
    public void TestCreateSplitPayment() throws Exception {

        Client client = createMockClientFromFile("mocks/authorise-success.json");
        Payment payment = new Payment(client);

        PaymentRequest paymentRequest = createBasePaymentRequest(new PaymentRequest()).reference("123456")
                                                                                      .setAmountData("6200", "EUR")
                                                                                      .setCardData("5136333333333335", "John Doe", "08", "2018", "737");

        // splitPayment
        SplitPayment splitPayment = new SplitPayment();
        splitPayment.setApi(1);
        splitPayment.setTotalAmount(new Long("6200"));
        splitPayment.setCurrencyCode("EUR");

        List<SplitPaymentItem> splitPaymentItems = new ArrayList<SplitPaymentItem>();

        // first split payment item
        SplitPaymentItem splitPaymentItem = new SplitPaymentItem();
        splitPaymentItem.setAmount(new Long(6000));
        splitPaymentItem.setType("MarketPlace");
        splitPaymentItem.setAccount("151272963");
        splitPaymentItem.setReference("6124145");
        splitPaymentItem.setDescription("Porcelain Doll: Eliza (20cm)");
        splitPaymentItems.add(splitPaymentItem);

        // seconf split payment item
        SplitPaymentItem splitPaymentItem2 = new SplitPaymentItem();
        splitPaymentItem2.setAmount(new Long(200));
        splitPaymentItem2.setType("Commission");
        splitPaymentItem2.setReference("6124146");
        splitPaymentItems.add(splitPaymentItem2);

        // add items to list
        splitPayment.setSplitPaymentItems(splitPaymentItems);

        // add it into the request
        paymentRequest.setSplitPayment(splitPayment);

        PaymentResult paymentResult = payment.authorise(paymentRequest);

        assertTrue(paymentResult.isAuthorised());

        SimpleDateFormat format = new SimpleDateFormat("M/yyyy");
        assertEquals("8/2018", format.format(paymentResult.getExpiryDate()));

        assertEquals("411111", paymentResult.getCardBin());
        assertEquals("1111", paymentResult.getCardSummary());
        assertEquals("Holder", paymentResult.getCardHolderName());
        assertTrue(paymentResult.get3DOffered());
        assertFalse(paymentResult.get3DAuthenticated());
        assertEquals("69746", paymentResult.getAuthCode());

        assertEquals(11, paymentResult.getFraudResult().getFraudCheckResults().size());

        FraudCheckResult fraudCheckResult = paymentResult.getFraudResult().getFraudCheckResults().get(0);
        assertEquals("CardChunkUsage", fraudCheckResult.getName());
        assertEquals(8, fraudCheckResult.getAccountScore().intValue());
        assertEquals(2, fraudCheckResult.getCheckId().intValue());
    }

    /**
     * Test success flow for POST /createAccountHolder
     */
    @Test
    public void TestCreateAccountHolderSuccess() throws Exception {

        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/create-account-holder-success.json");

        // use Account service
        Account account = new Account(client);

        // Create Account Holder Reqeust
        CreateAccountHolderRequest createAccountHolderRequest = new CreateAccountHolderRequest();
        createAccountHolderRequest.setAccountHolderCode("TestAccountHolder781664");

        // create AcountHolderDetails
        AccountHolderDetails accountHolderDetails = new AccountHolderDetails();

        // Add address to AcountHolderDetails
        Address address = new Address();
        address.setCity("Amsterdam");
        address.setCountry("US");
        address.houseNumberOrName("10");
        address.postalCode("12345");
        address.stateOrProvince("NH");
        address.setStreet("Teststreet 1");
        accountHolderDetails.setAddress(address);

        // set BankAccountDetails
        BankAccountDetail bankAccountDetail = new BankAccountDetail();
        bankAccountDetail.setAccountNumber("12345678");
        bankAccountDetail.setBankAccountName("baName");
        bankAccountDetail.setBankBicSwift("BicSUSft");
        bankAccountDetail.setBankCity("city");
        bankAccountDetail.setBankName("bankName");
        bankAccountDetail.setBranchCode("122105155");
        bankAccountDetail.setCountryCode("US");
        bankAccountDetail.setCurrencyCode("EUR");
        bankAccountDetail.setIban("iban");
        bankAccountDetail.setOwnerCity("ownerCity");
        bankAccountDetail.setOwnerCountryCode("BE");
        bankAccountDetail.setOwnerDateOfBirth("1980-01-01"); // set datetime element
        bankAccountDetail.setOwnerHouseNumberOrName("houseNumberOrName");
        bankAccountDetail.setOwnerName("ownerName");
        bankAccountDetail.setOwnerNationality("AD");
        bankAccountDetail.setOwnerPostalCode("ownerPostalCode");
        bankAccountDetail.setOwnerState("ownerState");
        bankAccountDetail.setOwnerStreet("ownerStreet");
        bankAccountDetail.setPrimaryAccount(true);
        bankAccountDetail.setTaxId("bankTaxId");
        accountHolderDetails.addBankAccountDetail(bankAccountDetail);
        accountHolderDetails.setEmail("test@adyen.com");

        // set individualDetails
        IndividualDetails individualDetails = new IndividualDetails();

        // set name inside individualDetails
        Name name = new Name();
        name.firstName("First name");
        name.lastName("Last Name");
        name.gender(Name.GenderEnum.MALE);
        individualDetails.setName(name);

        // set personal data inside individualDetails
        PersonalData personalData = new PersonalData();
        personalData.dateOfBirth("1970-01-01");
        personalData.setIdNumber("1234567890");
        personalData.setNationality("NL");
        individualDetails.setPersonalData(personalData);

        // attach individualDetails into accountHolderDetails
        accountHolderDetails.setIndividualDetails(individualDetails);
        accountHolderDetails.setMerchantCategoryCode("7999");

        // create phone number
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneCountryCode("NL");
        phoneNumber.setPhoneNumber("0612345678");
        phoneNumber.setPhoneType(PhoneNumber.PhoneTypeEnum.MOBILE);
        accountHolderDetails.setPhoneNumber(phoneNumber);
        accountHolderDetails.setWebAddress("http://www.adyen.com");

        // set accountHolderDetails
        createAccountHolderRequest.setAccountHolderDetails(accountHolderDetails);
        createAccountHolderRequest.setCreateDefaultAccount(true);
        createAccountHolderRequest.setLegalEntity(CreateAccountHolderRequest.LegalEntityEnum.INDIVIDUAL);

        // createAccountHolder
        CreateAccountHolderResponse createAccountHolderResponse = account.createAccountHolder(createAccountHolderRequest);

        System.out.println(createAccountHolderResponse);
        assertEquals("140922935", createAccountHolderResponse.getAccountCode());
    }


    /**
     * Test success flow for POST /accountHolderBalance
     */
    @Test
    public void TestAccountHolderBalanceSuccess() throws Exception {

        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/fund/account-holder-balance-success.json");

        // use Fund service
        Fund fund = new Fund(client);

        // create AccountHolderBalance Request
        AccountHolderBalanceRequest accountHolderBalanceRequest = new AccountHolderBalanceRequest();
        accountHolderBalanceRequest.accountHolderCode("TestAccountHolder877209");
        AccountHolderBalanceResponse accountHolderBalanceResponse = fund.AccountHolderBalance(accountHolderBalanceRequest);

        System.out.println(accountHolderBalanceResponse);
    }

    @Test
    public void TestUpdateAccountHolderSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/update-account-holder-success.json");

        // use Account service
        Account account = new Account(client);

        // Update Account Holder Request
        UpdateAccountHolderRequest updateAccountHolderRequest = new UpdateAccountHolderRequest();
        updateAccountHolderRequest.setAccountHolderCode("TestAccountHolder414261");

        // create AcountHolderDetails
        AccountHolderDetails accountHolderDetails = new AccountHolderDetails();

        // Add address to AcountHolderDetails
        Address address = new Address();
        address.setCity("Amsterdam");
        address.setCountry("NL");
        address.houseNumberOrName("10");
        address.postalCode("12345");
        address.stateOrProvince("NH");
        address.setStreet("Teststreet 1");
        accountHolderDetails.setAddress(address);
        accountHolderDetails.setEmail("test@adyen.com");

        // set individualDetails
        IndividualDetails individualDetails = new IndividualDetails();

        // update name inside individualDetails
        Name name = new Name();
        name.firstName("First name");
        name.lastName("Last Name");
        name.gender(Name.GenderEnum.MALE);
        individualDetails.setName(name);

        // update personal data inside individualDetails
        PersonalData personalData = new PersonalData();
        personalData.dateOfBirth("1970-01-01");
        personalData.setIdNumber("1234567890");
        personalData.setNationality("NL");
        individualDetails.setPersonalData(personalData);

        // attach individualDetails into accountHolderDetails
        accountHolderDetails.setIndividualDetails(individualDetails);
        accountHolderDetails.setMerchantCategoryCode("7999");

        // create phone number and set Web Address
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneCountryCode("NL");
        phoneNumber.setPhoneNumber("0612345678");
        phoneNumber.setPhoneType(PhoneNumber.PhoneTypeEnum.MOBILE);
        accountHolderDetails.setPhoneNumber(phoneNumber);
        accountHolderDetails.setWebAddress("http://www.accountholderwebsite.com");

        // updateAccountHolder
        updateAccountHolderRequest.setAccountHolderDetails(accountHolderDetails);
        UpdateAccountHolderResponse updateAccountHolderResponse = account.updateAccountHolder(updateAccountHolderRequest);
        System.out.println(updateAccountHolderResponse);

    }

    @Test
    public void TestGetIndividualAccountHolderSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/get-individual-account-holder-success.json");

        // use Account service
        Account account = new Account(client);

        // create GetAccountHolder Request
        GetAccountHolderRequest getAccountHolderRequest = new GetAccountHolderRequest();
        getAccountHolderRequest.setAccountHolderCode("TestAccountHolder3110");

        GetAccountHolderResponse getAccountHolderResponse = account.getAccountHolder(getAccountHolderRequest);
        System.out.println(getAccountHolderResponse);

    }

    @Test
    public void TestGetBusinessAccountHolderSuccess() throws Exception {

        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/get-business-account-holder-success.json");

        // use Account service
        Account account = new Account(client);

        // create GetAccountHolder Request
        GetAccountHolderRequest getAccountHolderRequest = new GetAccountHolderRequest();
        getAccountHolderRequest.setAccountHolderCode("TestAccountHolder480834");

        GetAccountHolderResponse getAccountHolderResponse = account.getAccountHolder(getAccountHolderRequest);
        System.out.println(getAccountHolderResponse);
    }

    @Test
    public void TestUploadDocumentSuccess() throws Exception {

        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/upload-document-success.json");

        // use Account service
        Account account = new Account(client);

        // Create UploadDocument Request
        UploadDocumentRequest uploadDocumentRequest = new UploadDocumentRequest();
        uploadDocumentRequest.setDocumentContent("dGVzdCBkb2N1bWVudCBjb250ZW50");

        // Create Document Detail
        DocumentDetail documentDetail = new DocumentDetail();
        documentDetail.setAccountHolderCode("TestAccountHolder534055");
        documentDetail.setDocumentType(DocumentDetail.DocumentTypeEnum.PASSPORT);
        documentDetail.setFilename("passport.png");
        documentDetail.setDescription("test passport description");

        // Set Document Detail
        uploadDocumentRequest.setDocumentDetail(documentDetail);

        UploadDocumentResponse uploadDocumentResponse = account.uploadDocument(uploadDocumentRequest);
        System.out.println(uploadDocumentResponse);
    }

    @Test
    public void TestTransferFundsSuccess() throws Exception {

        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/fund/transfer-funds-success.json");

        // use Fund service
        Fund fund = new Fund(client);

        // create TransferFunds Request
        TransferFundsRequest transferFundsRequest = new TransferFundsRequest();
        transferFundsRequest.setSourceAccountCode("100000000");
        transferFundsRequest.setDestinationAccountCode("190324759");

        // create Amount
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setValue(new Long(2000));
        transferFundsRequest.setAmount(amount);

        transferFundsRequest.setTransferCode("CODE_A");
        TransferFundsResponse transferFundsResponse = fund.transferFunds(transferFundsRequest);

        System.out.println(transferFundsResponse);
    }

    @Test
    public void TestCreateAccountSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/create-account-success.json");

        // use Account service
        Account account = new Account(client);

        // create CreateAccount Request
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setAccountHolderCode("TestAccountHolder5691");

        CreateAccountResponse createAccountResponse = account.createAccount(createAccountRequest);
        System.out.println(createAccountResponse);

    }

    @Test
    public void TestDeleteBankAccountSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/delete-bank-account-success.json");

        // use Account service
        Account account = new Account(client);

        // create DeleteBankAccount Request
        DeleteBankAccountRequest deleteBankAccountRequest = new DeleteBankAccountRequest();
        deleteBankAccountRequest.setAccountHolderCode("TestAccountHolder8843");
        deleteBankAccountRequest.addBankAccountUUIDsItem("eeb6ed22-3bae-483c-83b9-bc2097a75d40");

        DeleteBankAccountResponse deleteBankAccountResponse = account.deleteBankAccount(deleteBankAccountRequest);
        System.out.println(deleteBankAccountResponse);


    }

    @Test
    public void TestDeleteShareholderSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/delete-shareholder-success.json");

        // use Account service
        Account account = new Account(client);

        // create DeleteShareholder Request
        DeleteShareholderRequest deleteShareholderRequest = new DeleteShareholderRequest();
        deleteShareholderRequest.setAccountHolderCode("TestAccountHolder289429");
        deleteShareholderRequest.addShareholderCodesItem("9188218c-576e-4cbe-8e86-72722f453920");

        DeleteShareholderResponse deleteShareholderResponse = account.deleteShareholder(deleteShareholderRequest);
        System.out.println(deleteShareholderResponse);
    }

    @Test
    public void TestSuspendAccountHolderSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/suspend-account-holder-success.json");

        // use Account service
        Account account = new Account(client);

        // create SuspendAccountHolder Request
        SuspendAccountHolderRequest suspendAccountHolderRequest = new SuspendAccountHolderRequest();
        suspendAccountHolderRequest.setAccountHolderCode("TestAccountHolder192192");

        SuspendAccountHolderResponse suspendAccountHolderResponse = account.suspendAccountHolder(suspendAccountHolderRequest);
        System.out.println(suspendAccountHolderResponse);
    }

    @Test
    public void TestUnSuspendAccountHolderSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/un-suspend-account-holder-success.json");

        // use Account service
        Account account = new Account(client);

        // create UnSuspendAccountHolder Request
        UnSuspendAccountHolderRequest unSuspendAccountHolderRequest = new UnSuspendAccountHolderRequest();
        unSuspendAccountHolderRequest.setAccountHolderCode("TestAccountHolder192192");

        UnSuspendAccountHolderResponse unSuspendAccountHolderResponse = account.unSuspendAccountHolder(unSuspendAccountHolderRequest);
        System.out.println(unSuspendAccountHolderResponse);
    }

    @Test
    public void TestUpdateAccountHolderStateSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/update-account-holder-state-success.json");

        // use Account service
        Account account = new Account(client);

        // create UpdateAccountHolderState Request
        UpdateAccountHolderStateRequest updateAccountHolderStateRequest = new UpdateAccountHolderStateRequest();
        updateAccountHolderStateRequest.setAccountHolderCode("TestAccountHolder215394");
        updateAccountHolderStateRequest.setDisable(true);
        updateAccountHolderStateRequest.setReason("test reason payout");
        updateAccountHolderStateRequest.stateType(UpdateAccountHolderStateRequest.StateTypeEnum.PAYOUT);

        UpdateAccountHolderStateResponse updateAccountHolderStateResponse = account.updateAccountHolderState(updateAccountHolderStateRequest);
        System.out.println(updateAccountHolderStateResponse);
    }

    @Test
    public void TestCloseAccountSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/close-account-success.json");

        // use Account service
        Account account = new Account(client);

        // create CloseAccount Request
        CloseAccountRequest closeAccountRequest = new CloseAccountRequest();
        closeAccountRequest.setAccountCode("118731451");

        CloseAccountResponse closeAccountResponse = account.closeAccount(closeAccountRequest);
        System.out.println(closeAccountResponse);
    }

    @Test
    public void TestCloseAccountHolderSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/close-account-holder-success.json");

        // use Account service
        Account account = new Account(client);

        // create CloseAccountHolder Request
        CloseAccountHolderRequest closeAccountHolderRequest = new CloseAccountHolderRequest();
        closeAccountHolderRequest.setAccountHolderCode("TestAccountHolder1450");

        CloseAccountHolderResponse closeAccountHolderResponse = account.closeAccountHolder(closeAccountHolderRequest);
        System.out.println(closeAccountHolderResponse);
    }

    @Test
    public void TestPayoutAccountHolderSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/fund/payout-account-holder-success.json");

        // use Fund service
        Fund fund = new Fund(client);

        // create PayoutAccountHolder Request
        PayoutAccountHolderRequest payoutAccountHolderRequest = new PayoutAccountHolderRequest();
        payoutAccountHolderRequest.setAccountCode("118731451");
        payoutAccountHolderRequest.setAccountHolderCode("TestAccountHolder877209");
        payoutAccountHolderRequest.setDescription("12345 – Test");
        payoutAccountHolderRequest.setBankAccountUUID("000b81aa-ae7e-4492-aa7e-72b2129dce0c");

        // create Amount
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setValue(new Long(2000));
        payoutAccountHolderRequest.setAmount(amount);

        PayoutAccountHolderResponse payoutAccountHolderResponse = fund.payoutAccountHolder(payoutAccountHolderRequest);

        System.out.println(payoutAccountHolderResponse);
    }

    @Test
    public void TestUpdateAccountSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/update-account-success.json");

        // use Account service
        Account account = new Account(client);

        // create Update Account Request
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setAccountCode("198360329");

        UpdatePayoutScheduleRequest updatePayoutScheduleRequest = new UpdatePayoutScheduleRequest();
        updatePayoutScheduleRequest.setSchedule(UpdatePayoutScheduleRequest.ScheduleEnum.WEEKLY);
        updatePayoutScheduleRequest.setAction(UpdatePayoutScheduleRequest.ActionEnum.CLOSE);

        updateAccountRequest.setPayoutSchedule(updatePayoutScheduleRequest);

        UpdateAccountResponse updateAccountResponse = account.updateAccount(updateAccountRequest);
        System.out.println(updateAccountResponse);
    }

    @Test
    public void TestAccountHolderTransactionListSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/fund/account-holder-transaction-list-success.json");

        // use Fund service
        Fund fund = new Fund(client);

        // create AccountHolderTransactionList Request
        AccountHolderTransactionListRequest accountHolderTransactionListRequest = new AccountHolderTransactionListRequest();
        accountHolderTransactionListRequest.setAccountHolderCode("TestAccountHolder423978");
        accountHolderTransactionListRequest.addTransactionStatusesItem(AccountHolderTransactionListRequest.TransactionStatusesEnum.PENDINGCREDIT);
        accountHolderTransactionListRequest.addTransactionStatusesItem(AccountHolderTransactionListRequest.TransactionStatusesEnum.CREDITED);
        accountHolderTransactionListRequest.addTransactionStatusesItem(AccountHolderTransactionListRequest.TransactionStatusesEnum.DEBITED);
        TransactionListForAccount transactionListForAccount = new TransactionListForAccount();
        transactionListForAccount.setAccountCode("2e64b396-1200-4474-b848-0cb06b52b3c7");
        transactionListForAccount.setPage(2);
        accountHolderTransactionListRequest.addTransactionListsPerAccountItem(transactionListForAccount);


        AccountHolderTransactionListResponse accountHolderTransactionListResponse = fund.accountHolderTransactionList(accountHolderTransactionListRequest);
        System.out.println(accountHolderTransactionListResponse);
    }

    @Test
    public void TestGetUploadedDocumentsSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/account/get-uploaded-documents-success.json");

        // use Account service
        Account account = new Account(client);

        // create GetUploadedDocuments request
        GetUploadedDocumentsRequest getUploadedDocumentsRequest = new GetUploadedDocumentsRequest();
        getUploadedDocumentsRequest.setAccountHolderCode("TestAccountHolder8031");
        getUploadedDocumentsRequest.setBankAccountUUID("EXAMPLE_UUID");

        GetUploadedDocumentsResponse getUploadedDocumentsResponse = account.getUploadedDocuments(getUploadedDocumentsRequest);
        System.out.println(getUploadedDocumentsResponse);
    }

    @Test
    public void TestRefundNotPaidOutTransfersSuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/fund/refund-not-paid-out-transfers-success.json");

        // use Fund service
        Fund fund = new Fund(client);

        RefundNotPaidOutTransfersRequest refundNotPaidOutTransfersRequest = new RefundNotPaidOutTransfersRequest();
        refundNotPaidOutTransfersRequest.setAccountHolderCode("TestAccountHolder502924");
        refundNotPaidOutTransfersRequest.setAccountCode("189184578");

        RefundNotPaidOutTransfersResponse refundNotPaidOutTransfersResponse = fund.refundNotPaidOutTransfers(refundNotPaidOutTransfersRequest);
        System.out.println(refundNotPaidOutTransfersResponse);
    }

    @Test
    public void TestSetupBeneficiarySuccess() throws Exception {
        // setup client
        Client client = createMockClientFromFile("mocks/marketpay/fund/setup-beneficiary-success.json");

        // use Fund service
        Fund fund = new Fund(client);

        SetupBeneficiaryRequest setupBeneficiaryRequest = new SetupBeneficiaryRequest();
        setupBeneficiaryRequest.setDestinationAccountCode("128952522");
        setupBeneficiaryRequest.setMerchantReference("Test");
        setupBeneficiaryRequest.setSourceAccountCode("134498192");

        SetupBeneficiaryResponse setupBeneficiaryResponse = fund.setupBeneficiary(setupBeneficiaryRequest);
        System.out.println(setupBeneficiaryResponse);
    }

}
