package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    boolean result = false;
    List<String> listOperators = new ArrayList<String>();
    List<String> listNumbers = new ArrayList<String>();
    List<String> tempOperatorList = new ArrayList<String>();
    List<Integer> tempNumberList = new ArrayList<Integer>();
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listOperators.add("/");
        listOperators.add("*");
        listOperators.add("+");
        listOperators.add("-");
        listNumbers.add("0");
        listNumbers.add("1");
        listNumbers.add("2");
        listNumbers.add("3");
        listNumbers.add("4");
        listNumbers.add("5");
        listNumbers.add("6");
        listNumbers.add("7");
        listNumbers.add("8");
        listNumbers.add("9");

    }

    public void onClickBtn(View v){

        TextView textView = (TextView)findViewById(R.id.textView);
        Button b = (Button)v;
        if (result == true){
            textView.setText("");

            result = false;
        }

        String buttonText = b.getText().toString();
        if(buttonText.equals("Clear")){
            textView.setText("");
            System.out.println(buttonText);
            tempNumberList.removeAll(tempNumberList);
            tempOperatorList.removeAll(tempOperatorList);
            flag=0;
        }
        else if(buttonText.equals("=")){
//            ScriptEngine
            int answer = resultEvaluator();
            String answer_string = Integer.toString(answer);
            textView.setText(answer_string.toString());
            result = true;
            flag=0;

        }
        else{
            textView.append(buttonText);
//            int tempVar;
            if(listNumbers.contains(buttonText)){
                int i = Integer.parseInt(buttonText);
                if (flag > 0){
                    int tempVar = tempNumberList.get(tempNumberList.size()-1);
//                    tempVar = (tempVar*10)+i;
                    int tempVar1 = tempVar*10+i;
                    tempNumberList.remove(tempNumberList.size()-1);
                    tempNumberList.add(tempNumberList.size(), tempVar1);

                }
                else {
                    tempNumberList.add(i);
                }

                flag = flag+1;

            }

            else if(listOperators.contains(buttonText)){
                tempOperatorList.add(buttonText);
                flag = 0;
            }
        }

    }
    public int resultEvaluator(){
        System.out.println(tempOperatorList);
        int finalRes = 0;
        while (tempOperatorList.size() != 0){
            for(int i=0; i<listOperators.size();i++){

                String refOperator = listOperators.get(i);
                for(int j=0; j<tempOperatorList.size();j++){
                    String operator = tempOperatorList.get(j);
                    if (operator.equals(refOperator)){
                        int operand1 = tempNumberList.get(j);
                        int operand2 = tempNumberList.get(j+1);
//                    int value = j+1;
                        tempNumberList.remove(j);
                        tempNumberList.remove(j);
                        if (operator.equals("/")){
                            finalRes = operand1/operand2;
                        }
                        else if (operator.equals("*")){
                            finalRes = operand1*operand2;
                        }

                        else if (operator.equals("+")){
                            finalRes = operand1+operand2;
                        }

                        else if (operator.equals("-")){
                            finalRes = operand1-operand2;
                        }

                        tempOperatorList.remove(j);
                        tempNumberList.add(j, finalRes);
                    }
                }
            }

        }

        tempNumberList.removeAll(tempNumberList);
        return finalRes;
    }
}
