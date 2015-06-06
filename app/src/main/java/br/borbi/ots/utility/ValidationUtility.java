package br.borbi.ots.utility;

import android.widget.EditText;

/**
 * Created by Gabriela on 06/06/2015.
 */
public class ValidationUtility {

    /*
    Valida numeros inteiros (menor que minNumber, maior que maxNumber).
     */
    public static boolean validateInteger(EditText editText, Integer minNumber, Integer maxNumber, String message) {
        if(editText.getText() == null){
            editText.setError(message);
            return false;
        }
        String value = editText.getText().toString();
        if ("".equals(value.trim())) {
            editText.setError(message);
            return false;
        }

        int number = Integer.valueOf(value);
        if ((minNumber != null && number < minNumber) || (maxNumber != null && number > maxNumber)) {
            editText.setError(message);
            return false;
        }
        return true;
    }


}
