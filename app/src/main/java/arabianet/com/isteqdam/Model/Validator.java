package arabianet.com.isteqdam.Model;

public class Validator {

    public Boolean validateMobileNumber(String mobileNumber){
        if(mobileNumber.length() < 7){
            return false;
        }else{
            return true;
        }
    }

    public Boolean validateConfirmCode(String confirmCode){
        if(confirmCode.length() != 6){
            return false;
        }else{
            return true;
        }
    }

    public Boolean validateName(String name){
        if(name.length() >= 4){
            return true;
        }else{
            return false;
        }
    }

    public Boolean validateEmail(String Email){
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(Email);
        return m.matches();
    }


}
