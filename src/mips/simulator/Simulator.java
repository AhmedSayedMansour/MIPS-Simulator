package mips.simulator;

import java.util.ArrayList;
import java.util.HashMap;

public class Simulator {
    Assembler AssemblerObject ;
    HashMap<String, Integer> Registers = new HashMap<>();   //Registers and its values
    HashMap<String, Integer> Memories = new HashMap<>();    //Used memories
    ArrayList<Assembler.MachineSet> kernelSet = new ArrayList<>();

    public Simulator(Assembler obj){
        AssemblerObject = obj;
        //fill Registers
        clearRegisters();
        //fill memories
        /*text segment*/
            //filled in gui
        /*data segment*/
            //filled by GUI
        //fill kernel set
        kernelSet = kernel(obj);
        clearRegisters();
        clearMemories();
    }

    public ArrayList<Assembler.MachineSet> kernel(Assembler obj){
        ArrayList<Assembler.MachineSet> ins = new ArrayList<Assembler.MachineSet>();
        for (int i=0; i<obj.Instructions.size(); i++){
            if(obj.Instructions.get(i).fields[0].equals("beq")){///if beq
                int rs =  Registers.get(obj.Instructions.get(i).fields[1]);
                int rd =  Registers.get(obj.Instructions.get(i).fields[2]);
                if (rs==rd){
                    if (obj.Labels.get(obj.Instructions.get(i).fields[3])>obj.Instructions.size())break;
                    else i = obj.Labels.get(obj.Instructions.get(i).fields[3])-1;
                }
            }else if (obj.Instructions.get(i).fields[0].equals("bne")){///if bne
                int rs =  Registers.get(obj.Instructions.get(i).fields[1]);
                int rd =  Registers.get(obj.Instructions.get(i).fields[2]);
                if (rs!=rd){
                    if (obj.Labels.get(obj.Instructions.get(i).fields[3])>obj.Instructions.size())break;
                    else i = obj.Labels.get(obj.Instructions.get(i).fields[3])-1;
                }
            }else if(obj.Instructions.get(i).fields[0].equals("j")){
                if (obj.Labels.get(obj.Instructions.get(i).fields[1])>obj.Instructions.size())break;
                else i = obj.Labels.get(obj.Instructions.get(i).fields[1])-1;
            }else if (obj.Instructions.get(i).fields[0].equals("jr")){
                if (Registers.get(obj.Instructions.get(i).fields[3])>obj.Instructions.size())break;
                else i = obj.Labels.get(obj.Instructions.get(i).fields[3])-1;
            }
            else if(!obj.Instructions.get(i).fields[0].contains(":")){
                runInstruction(obj.Instructions.get(i));
                ins.add(obj.Instructions.get(i));
            }
        }
        return ins;
    }

    public void clearRegisters(){
        Registers.clear();
        Registers.put("$0",   0);   Registers.put("$at", 0  );   Registers.put("$v0", 0);   Registers.put("$v1", 0);    Registers.put("$a0", 0);
        Registers.put("$a1",  0);   Registers.put("$a2", 0  );   Registers.put("$a3", 0);   Registers.put("$t0", 0);    Registers.put("$t1", 0);
        Registers.put("$t2",  0);   Registers.put("$t3", 0  );   Registers.put("$t4", 0 );  Registers.put("$t5", 0 );   Registers.put("$t6", 0);
        Registers.put("$t7",  0);   Registers.put("$s0", 0  );   Registers.put("$s1", 0 );  Registers.put("$s2", 0 );   Registers.put("$s3", 0);
        Registers.put("$s4",  0);   Registers.put("$s5", 0  );   Registers.put("$s6", 0 );  Registers.put("$s7", 0 );   Registers.put("$t8", 0);
        Registers.put("$t9",  0);   Registers.put("$k0", 0  );   Registers.put("$k1", 0 );  Registers.put("$gp", 0 );   Registers.put("$sp", 0);
        Registers.put("$fp",  0);   Registers.put("$ra", 0  );
    }
    
    public void clearMemories(){
        Memories.clear();
    }
    
    public void runInstruction (Assembler.MachineSet ins){
        switch(ins.fields[0]){
            case "lw":      //Load word
                String []arr = ins.fields[2].split("\\(");
                arr[1] = arr[1].substring(0, arr[1].length()-1);
                int addr = Integer.parseInt(arr[0])+ Registers.get(arr[1]);
                if(Memories.containsKey(AssemblerObject.Decimalto8Hexa(addr+4096))){
                    Registers.replace(ins.fields[1], Memories.get(AssemblerObject.Decimalto8Hexa(addr)));
                }
                else{
                    Registers.replace(ins.fields[1], 0);
                    Memories.put(AssemblerObject.Decimalto8Hexa(addr+4096), 0);
                }
                break;
            case "sw":      //save word
                arr = ins.fields[2].split("\\(");
                arr[1] = arr[1].substring(0, arr[1].length()-1);
                addr = Integer.parseInt(arr[0])+ Registers.get(arr[1]);
                if(Memories.containsKey(AssemblerObject.Decimalto8Hexa(addr+4096))){
                    Memories.replace(AssemblerObject.Decimalto8Hexa(addr+4096),Registers.get(ins.fields[1]));
                }
                else{
                    Memories.put(AssemblerObject.Decimalto8Hexa(addr+4096), Registers.get(ins.fields[1]));
                }
                break;
            case "add":     //add two Registers
                Registers.replace(ins.fields[1], Registers.get(ins.fields[2])+ Registers.get(ins.fields[3]));
                break;
            case "addi":    //add rejester to number
                Registers.replace(ins.fields[1], Registers.get(ins.fields[2])+ Integer.parseInt(ins.fields[3]));
                break;
            case "sub":     //subtruct two Registers
                Registers.replace(ins.fields[1], Registers.get(ins.fields[2])- Integer.parseInt(ins.fields[3]));
                break;
            case "and":     //and two Registers
                Registers.replace(ins.fields[1], Registers.get(ins.fields[2])& Registers.get(ins.fields[3]));
                break;
            case "or":      //or two Registers
                Registers.replace(ins.fields[1], Registers.get(ins.fields[2])| Registers.get(ins.fields[3]));
                break;
            case "andi":    //and rejister with value
                Registers.replace(ins.fields[1], Registers.get(ins.fields[2])& Integer.parseInt(ins.fields[3]));
                break;
            case "ori":     //or rejister with value
                Registers.replace(ins.fields[1], Registers.get(ins.fields[2])| Integer.parseInt(ins.fields[3]));
                break;
            case "sll":     //Shift left logical "<<"
                Registers.replace(ins.fields[1], Registers.get(ins.fields[2])<< Registers.get(ins.fields[3]));
                break;
            case "slt":     //Set on less than
                Registers.replace(ins.fields[1], Registers.get(ins.fields[2])< Registers.get(ins.fields[3]) ? 1 : 0);
                break;
            case "slti":    //Set on less than immediate
                Registers.replace(ins.fields[1], Registers.get(ins.fields[2])< Integer.parseInt(ins.fields[3]) ? 1 : 0);
                break;
            case "lui":     //Load upper immediate
                
                break;
        }
    }
}
