package logic;

import com.thoughtworks.qdox.model.expression.ShiftRight;
import common.ValidationException;
import dal.AccountDAL;
import entity.Account;
import org.hibernate.property.access.internal.PropertyAccessStrategyIndexBackRefImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public class AccountLogic extends GenericLogic<Account,AccountDAL>{
    
    public static final String DISPLAY_NAME = "displayName";
    public static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String ID = "id";
    
    public AccountLogic() {
        super(new AccountDAL());
    }
    
    @Override
    public List<Account> getAll(){
        return get(()->dao().findAll());
    }
    
    public List<Account> getAllWithAnonymousClass(){
        //method get() in superclass GenericLogic needs a Supplier which is an interface.
        //Supplier interface is like below:
        //interface Supplier<T>{
        //  T get();
        //}
        //we can create an anonymous instance of Supplier
        Supplier<List<Account>> supply = new Supplier<List<Account>>() {
            @Override
            public List<Account> get() {
                return dao().findAll();
            }
        };
        //this is long and has more code, it can be replaced with lambda or method reference
        return get(supply);
    }
    
    public List<Account> getAllWithLambda(){
        //method get() in superclass GenericLogic needs a Supplier which is a functional interface.
        //functional interface has one method, meaning it can be initialized with lambda.
        //Supplier interface is like below:
        //interface Supplier<T>{
        //  T get();
        //}
        Supplier<List<Account>> supply = ()->{return dao().findAll();};
        //we can clean this lambda more. if your lambda has one line of code
        //the "{}" can be removed same as "return" which compiler can infer.
        supply = ()->dao().findAll();
        return get(supply);
        //all can be done in one line as well
        //return get(()->dao().findAll());
    }
    
    public List<Account> getAllWithMethodRefrence(){
        //method get() in superclass GenericLogic needs a Supplier which is a functional interface.
        //functional interface has one method, meaning it can be initialized with method reference.
        //Supplier interface is like below:
        //interface Supplier<T>{
        //  T get();
        //}
        Supplier<List<Account>> supply = null;// = dao()::findAll;
        //in this case we are using method refrence, we directly point to the location
        //which has the method and let the compiler deal with the rest.
        return get(supply);
        //all can be done in one line as well
        //return get(dao()::findAll);
    }
    
    @Override
    public Account getWithId( int id){
        return get(()->dao().findById(id));
    }
    
    public Account getAccountWithDisplayName( String displayName){
        return get(()->dao().findByDisplayName( displayName));
    }
    
    public Account getAccountWithUser( String user){
        return get(()->dao().findByUser(user));
    }
    
    public List<Account> getAccountsWithPassword( String password){
        return get(()->dao().findByPassword(password));
    }
    
    public Account getAccountWith( String username, String password){
        return get(()->dao().validateUser( username, password));
    }
    
    @Override
    public List<Account> search( String search){
        return get(()->dao().findContaining(search));
    }

    @Override
    public Account createEntity(Map<String, String[]> parameterMap) {
        Account account = new Account();
        String name = parameterMap.get(DISPLAY_NAME)[0];
        String user = parameterMap.get(USER)[0];
        String password = parameterMap.get(PASSWORD)[0];

        if(parameterMap.containsKey(ID)){
            String id = parameterMap.get(ID)[0];
            try {
                account.setId(Integer.parseInt(id));
            }
            catch (NumberFormatException ex){
                throw new ValidationException("Id should be number");
            }
//            if (!id.matches("^[0-9]*$")){
//                throw new ValidationException("Account Id must be numerical");
//            }
//            else account.setId(Integer.parseInt(id));
        }

        if (name == null ||name.isEmpty()){
            throw new ValidationException("Name cannot be empty or null");
        }
        else if (name.length()>45){
            throw new ValidationException("Name must be less than 45 characters");
        }
        else account.setDisplayName(name);


        if (user==null || user.isEmpty()){
            throw new ValidationException("Username cannot be empty or null");
        }
        else if (user.length()>45){
            throw new ValidationException("Username must be less than 45 characters");
        }
        else account.setUser(user);


        if(password==null || password.isEmpty()){
            throw new ValidationException("Password cannot be empty or null");
        }
        else if (password.length()>45){
            throw new ValidationException("Password must be less than 45 characters");
        }
        else account.setPassword(password);

        return account;
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Display Name", "User", "Password");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, DISPLAY_NAME, USER, PASSWORD);
    }

    @Override
    public List<?> extractDataAsList( Account e) {
        return Arrays.asList(e.getId(), e.getDisplayName(), e.getUser(), e.getPassword());
    }
}
