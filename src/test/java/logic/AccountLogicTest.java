package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.Account;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 *
 * @author Shariar
 */
class AccountLogicTest {

    public static Tomcat tomcat;
    public static AccountLogic logic;
    public static Map<String, String[]> sampleMap;
   

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        System.out.println(new File("src\\main\\webapp\\").getAbsolutePath());
        tomcat = new Tomcat();
        tomcat.enableNaming();
        tomcat.setPort(8080);
        Context context = tomcat.addWebapp("/WebScraper", new File("src\\main\\webapp").getAbsolutePath());
        context.addApplicationListener("dal.EMFactory");
        tomcat.init();
        tomcat.start();
    }
    
  

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        tomcat.stop();
    }

    @BeforeEach
    final void setUp() throws Exception {
        logic = new AccountLogic();
       
         sampleMap = new HashMap<>();
         sampleMap.put(AccountLogic.DISPLAY_NAME, new String[]{"JUnit 5 test"});
         sampleMap.put(AccountLogic.USER, new String[]{"junit"});
         sampleMap.put(AccountLogic.PASSWORD, new String[]{"junit5"});

    }

    @AfterEach
    final void tearDown() throws Exception {
    }
    

    @Test
    final void testGetAll() {
        
        //get all the accounts from the DB
        List<Account> list = logic.getAll();
        assertEquals(list.size(), 1);
        
        //store the size of list/ this way we know how many accounts exits in DB
        int originalSize = list.size();

        //create a new Account and save it so we can delete later
        Account testAccount = logic.createEntity(sampleMap);
        //add the newly created account to DB
        logic.add(testAccount);

        //get all the accounts again
        list = logic.getAll();
        //the new size of accounts must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());

        //delete the new account, so DB is reverted back to it original form
        logic.delete(testAccount);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be same as original size
        assertEquals(originalSize, list.size());
    }
    
    
   
    @Test
    final void testGetWithId(){
        
        //get all accounts
        List<Account> list = logic.getAll();
        //use the first account in the list as test Account
        Account testAccount = list.get(0);
        //using the id of test account get another account from logic
        Account returnedAccount = logic.getWithId(testAccount.getId());
        //the two accounts (testAcounts and returnedAccounts) must be the same
        //assert all field to guarantee they are the same
        assertEquals(testAccount.getId(), returnedAccount.getId());
        assertEquals(testAccount.getDisplayName(), returnedAccount.getDisplayName());
        assertEquals(testAccount.getUser(), returnedAccount.getUser());
        assertEquals(testAccount.getPassword(), returnedAccount.getPassword());    
    
    }
    
    @Test
    final void testGetAccountWithDisplayName(){
    
        List<Account> list = logic.getAll();
        Account testAccount = list.get(0);
        Account returnedAccount = logic.getAccountWithDisplayName(testAccount.getDisplayName());

        assertEquals(testAccount.getId(), returnedAccount.getId());
        assertEquals(testAccount.getDisplayName(), returnedAccount.getDisplayName());
        assertEquals(testAccount.getUser(), returnedAccount.getUser());
        assertEquals(testAccount.getPassword(), returnedAccount.getPassword());   
        
    }
    
     
    @Test
   final void testGetAccountWithUser(){
    
        List<Account> list = logic.getAll();
        Account testAccount = list.get(0);
        Account returnedAccount = logic.getAccountWithUser(testAccount.getUser());

        assertEquals(testAccount.getId(), returnedAccount.getId());
        assertEquals(testAccount.getDisplayName(), returnedAccount.getDisplayName());
        assertEquals(testAccount.getUser(), returnedAccount.getUser());
        assertEquals(testAccount.getPassword(), returnedAccount.getPassword()); 
    
    }
    
    @Test
    final void testGetAccountsWithPassword(){
        
        List<Account> list = logic.getAll();
        Account testAccount = list.get(0);       
        List<Account> returnedAccounts = logic.getAccountsWithPassword(testAccount.getPassword());

        for (int i=0; i <returnedAccounts.size(); i++){
            
            assertEquals(testAccount.getPassword(), returnedAccounts.get(i).getPassword()); 
        }

    }
    
     
    @Test   
    final void testGetAccountWith(){
        
        List<Account> list = logic.getAll();
        Account testAccount = list.get(0);
        Account returnedAccount = logic.getAccountWith(testAccount.getUser(), testAccount.getPassword());
        
        assertEquals(testAccount.getId(), returnedAccount.getId());
        assertEquals(testAccount.getDisplayName(), returnedAccount.getDisplayName());
        assertEquals(testAccount.getUser(), returnedAccount.getUser());
        assertEquals(testAccount.getPassword(), returnedAccount.getPassword()); 
        
    }
    
   
    @Test
    public void testSearch(){
        
        List<Account> list = logic.getAll();
        Account testAccount = list.get(0);
        List<Account> returnedAccounts = logic.search(testAccount.getUser());
        
        for (int i=0; i < returnedAccounts.size(); i++){

        assertTrue(returnedAccounts.get(i).getDisplayName().contains(list.get(0).getDisplayName()) ||
                   returnedAccounts.get(i).getUser().contains(list.get(0).getUser()));
           
       }
    }

    @Test
    public void testCreateEntity() {
        
     
       Account testAccount = logic.createEntity(sampleMap);
       assertEquals(testAccount.getDisplayName(), sampleMap.get(AccountLogic.DISPLAY_NAME)[0]);
       assertEquals(testAccount.getPassword(), sampleMap.get(AccountLogic.PASSWORD)[0]);
       assertEquals(testAccount.getUser(), sampleMap.get(AccountLogic.USER)[0]);

    } 

    
    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        List<?> hardCodedList = Arrays.asList("ID", "Display Name", "User", "Password");
        assertIterableEquals(list, hardCodedList);
    }

    
    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        List<?> hardCodedList = Arrays.asList(AccountLogic.ID, AccountLogic.DISPLAY_NAME, AccountLogic.USER, AccountLogic.PASSWORD);
        assertIterableEquals(list, hardCodedList);
    }

    
    
    @Test
    final void testExtractDataAsList() {
        Account testAccount = logic.getAll().get(0);
        assertEquals(Arrays.asList(testAccount.getId(), testAccount.getDisplayName(), testAccount.getUser(), testAccount.getPassword()), logic.extractDataAsList(testAccount));
    }
    
}
