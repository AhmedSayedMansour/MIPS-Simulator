package mips.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Assembler {

    ArrayList<String> Resgisters = new ArrayList<String>(Arrays.asList("$0", "$at", "$v0", "$v1",
            "$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$s0", "$s1",
            "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8", "$t9", "$k0", "$k1", "$gp", "$sp", "$fp", "$ra"));
    HashMap<String, String> opCodes = new HashMap<>();
    public String[] sets;   //line by line
    public ArrayList<MachineSet> Instructions = new ArrayList<>();
    public HashMap<String, Integer> Labels = new HashMap<>();    //jupms names(name,line)

    public class MachineSet {

        public String set;
        public Character type;
        public String hexaCode;
        public String[] fields ;
        ArrayList<String> Binary;

        public MachineSet(String ins,Character type, String data, String hexa, ArrayList<String> bin) {
            this.type = type;
            fields = data.split(" ");
            hexaCode = hexa;
            Binary = bin;
            set = ins;
        }
    }

    public Assembler() {
        //Adding all opcodes
        opCodes.put("lw", "100011");   opCodes.put("sw","101011" );   opCodes.put("addi", "001000");  opCodes.put("andi", "001100");   opCodes.put("ori", "001101");
        opCodes.put("slti", "001010");   opCodes.put("lui", "001111");  opCodes.put("beq", "000100");   opCodes.put("bne", "000101");   opCodes.put("add", "000000");
        opCodes.put("sub", "000000");   opCodes.put("and", "000000");   opCodes.put("or", "000000"); opCodes.put("sll", "000000");  opCodes.put("slt", "000000");   
        opCodes.put("jr", "000000");   opCodes.put("j", "000010");
    }

    public int checkValidation(String code) {
        code = code.replaceAll(",", " ");
        code = code.replaceAll(" +", " ");
        String[] elements = code.split(" ");
        switch (elements[0]) {
            //I-type: 
            case "lw":
            case "sw":
            case "lui":
                if (elements.length == 3 && Resgisters.contains(elements[1])) {
                    return 1;
                }
                else{
                    return -1;
                }
            case "addi":
            case "andi":
            case "ori":
            case "slti":
            case "beq":
            case "bne":
                if (elements.length == 4 && Resgisters.contains(elements[1]) && Resgisters.contains(elements[2])) {
                    return 1;
                }
                else{
                    return -1;
                }

            //R-type: 
            case "add":
            case "sub":
            case "and":
            case "or":
            case "slt":
                if (elements.length == 4 && Resgisters.contains(elements[1]) && Resgisters.contains(elements[2]) && Resgisters.contains(elements[3])) {
                    return 2;
                }
                else{
                    return -1;
                }
            case "sll":
                if (elements.length == 4 && Resgisters.contains(elements[1]) && Resgisters.contains(elements[2])) {
                    return 2;
                }
                else{
                    return -1;
                }
            case "jr":
                if (elements.length == 2 && Resgisters.contains(elements[1])) {
                    return 2;
                }
                else{
                    return -1;
                }
                
            //J-type: 
            case "j":
                if (elements.length == 2) {
                    return 3;
                }
                else{
                    return -1;
                }
            default:
                return 0;
        }
    }

    public Boolean CheckAndAdd(String full) {
        sets = full.split("\\n");
        String []code = full.split("\\n");
        for(int i=0; i < code.length;++i){
            String INS = code[i];
            code[i] = code[i].replaceAll(",", " ");
            code[i] = code[i].replaceAll(" +", " ");
            if (checkValidation(code[i]) > 0) {
                switch (checkValidation(code[i])) {
                    case 1:
                        Instructions.add(new MachineSet(INS,'I', code[i], toHexa(toBinary(code[i].split(" "),1)), toBinary(code[i].split(" "),1)));
                        break;
                    case 2:
                        Instructions.add(new MachineSet(INS,'R', code[i], toHexa(toBinary(code[i].split(" "),2)), toBinary(code[i].split(" "),2)));
                        break;
                    case 3:
                        Instructions.add(new MachineSet(INS,'J', code[i], toHexa(toBinary(code[i].split(" "),3)), toBinary(code[i].split(" "),3)));
                        break;
                }
            }
            else if(checkValidation(code[i]) == 0){    //not a code may be label
                if((!code[i].contains(" ")) && code[i].charAt(code[i].length()-1) == ':'){
                    Labels.put(code[i].substring(0, code[i].length()-1), i+1);
                    Instructions.add(new MachineSet(INS,'L', code[i], "0x00000000", toBinary(code[i].split(" "),4)));
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        return true;
    }

    public String toHexa(ArrayList<String> binaries ) {
        String binary = "";
        for(int i = 0 ; i< binaries.size() ; ++i){
            binary += binaries.get(i);
        }
        Long decimal=Long.parseLong(binary,2);
        String hexa = "0x" + Long.toHexString(decimal);

        return hexa;
    }
    
    public String Decimalto8Hexa(int Decimal){
        String hexa = Integer.toHexString(Decimal);
        while(hexa.length() < 8)
        {
           hexa = "0"+ hexa;
        }
        hexa = "0x" + hexa;
        
        return hexa;
    }

    public String binaryFromInt(int number , int size)
    {
        String binary = Integer.toBinaryString(number);
        while(binary.length() < size) {
            binary = "0" + binary;
        }
        return binary;
    }
    
    public ArrayList<String> toBinary(String[] code, Integer i) {
        ArrayList<String> s = new ArrayList<>();
        if (i == 1) {   //I-type
            s.add(opCodes.get(code[0]));  //op
            if(code[0].equals("lw") || code[0].equals("sw")){
                //code[2].replaceAll("\\)", "");
                code[2] = code[2].substring(0,code[2].length()-1);
                String[] arr = code[2].split("\\(");
                s.add(binaryFromInt(Resgisters.indexOf(arr[1]) , 5));  //rs
                s.add(binaryFromInt(Resgisters.indexOf(code[1]) , 5));  //rt
                s.add(binaryFromInt(Integer.parseInt(arr[0]) , 16));  //immediate
            }
            else if(code[0].equals("lui")){
                s.add("00000");  //rs
                s.add(binaryFromInt(Resgisters.indexOf(code[1]) , 5));  //rt
                s.add(binaryFromInt(Integer.parseInt(code[2]) , 16));  //immediate
            }
            else if(code[0].equals("beq") || code[0].equals("bne")){
                s.add(binaryFromInt(Resgisters.indexOf(code[1]) , 5));  //rs
                s.add(binaryFromInt(Resgisters.indexOf(code[2]) , 5));  //rt
                s.add(binaryFromInt(0 , 16));  //immediate              /***TODO***/
            }
            else{
                s.add(binaryFromInt(Resgisters.indexOf(code[2]) , 5));  //rs
                s.add(binaryFromInt(Resgisters.indexOf(code[1]) , 5));  //rt
                s.add(binaryFromInt(Integer.parseInt(code[3]) , 16));  //immediate
            }
        }
        else if (i == 2) {  //R-type
            s.add("000000");  //op
            if(code[0].equals("sll") ){
                s.add("00000");  //rs
                s.add(binaryFromInt(Resgisters.indexOf(code[2]) , 5));  //rt
                s.add(binaryFromInt(Resgisters.indexOf(code[1]) , 5));  //rd
                s.add(binaryFromInt(Integer.parseInt(code[3]) , 5));  //shamt
                s.add("000000");  //funct
            }
            else if(code[0].equals("jr")){
                s.add(binaryFromInt(Resgisters.indexOf(code[1]) , 5));  //rs
                s.add("00000");  //rt
                s.add("00000");  //rd
                s.add("00000");  //shamt
                s.add("001000");  //funct
            }
            else {
                s.add(binaryFromInt(Resgisters.indexOf(code[2]) , 5));  //rs
                s.add(binaryFromInt(Resgisters.indexOf(code[3]) , 5));  //rt
                s.add(binaryFromInt(Resgisters.indexOf(code[1]) , 5));  //rd
                s.add("00000");  //shamt
                if (code[0].equals("add"))   s.add(binaryFromInt(32 , 6));  //funct
                if (code[0].equals("sub"))   s.add(binaryFromInt(34 , 6));  //funct
                if (code[0].equals("and"))   s.add(binaryFromInt(36 , 6));  //funct
                if (code[0].equals("or"))   s.add(binaryFromInt(37 , 6));  //funct
                if (code[0].equals("slt"))   s.add(binaryFromInt(42 , 6));  //funct
            }
        }
        else if (i == 3) {  //J-type
            s.add("000010");    //Op
            s.add(binaryFromInt(0 , 26));          //addr      /***TODO***/
        }
        else{
            s.add(binaryFromInt(0 , 32));
        }
        return s;
    }
}
