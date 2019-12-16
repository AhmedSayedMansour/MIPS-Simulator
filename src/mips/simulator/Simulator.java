package mips.simulator;

import java.util.ArrayList;
import java.util.HashMap;

public class Simulator {
    Assembler AssemblerObject ;
    HashMap<String, Integer> Rejisters = new HashMap<>();   //Rejisters and its values
    HashMap<String, String> Memories = new HashMap<>();    //Used memories
    ArrayList<Assembler.MachineSet> kernelSet = new ArrayList<>();

    public Simulator(Assembler obj){
        AssemblerObject = obj;
        //fill Rejisters
        clearRejisters();
        //fill memories
        /*text segment*/
        int pos = 0;
        for(int i=0 ; i<obj.sets.length ;++i){
            if(!obj.sets[i].contains(":")){
                Memories.put(obj.Decimalto8Hexa(pos), obj.sets[i]);
                pos+=4;
            }
        }
        //fill kernel set
        kernelSet = kernel(obj);
    }

    public ArrayList<Assembler.MachineSet> kernel(Assembler obj){
        ArrayList<Assembler.MachineSet> ins = new ArrayList<Assembler.MachineSet>();
        for (int i=0; i<obj.Instructions.size(); i++){
            if(obj.Instructions.get(i).fields[0].equals("beq")){///if beq
                int rs =  Rejisters.get(obj.Instructions.get(i).fields[1]);
                int rd =  Rejisters.get(obj.Instructions.get(i).fields[2]);
                if (rs==rd){
                    if (obj.Labels.get(obj.Instructions.get(i).fields[3])>obj.Instructions.size())break;
                    else i = obj.Labels.get(obj.Instructions.get(i).fields[3])-1;
                }
            }else if (obj.Instructions.get(i).fields[0].equals("bne")){///if bne
                int rs =  Rejisters.get(obj.Instructions.get(i).fields[1]);
                int rd =  Rejisters.get(obj.Instructions.get(i).fields[2]);
                if (rs!=rd){
                    if (obj.Labels.get(obj.Instructions.get(i).fields[3])>obj.Instructions.size())break;
                    else i = obj.Labels.get(obj.Instructions.get(i).fields[3])-1;
                }
            }else if(obj.Instructions.get(i).fields[0].equals("j")){
                if (obj.Labels.get(obj.Instructions.get(i).fields[1])>obj.Instructions.size())break;
                else i = obj.Labels.get(obj.Instructions.get(i).fields[1])-1;
            }else if (obj.Instructions.get(i).fields[0].equals("jr")){
                if (Rejisters.get(obj.Instructions.get(i).fields[3])>obj.Instructions.size())break;
                else i = obj.Labels.get(obj.Instructions.get(i).fields[3])-1;
            }
            else{
                if (obj.Instructions.get(i).fields[0].equals("add")){
                    int tmp1 = Rejisters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Rejisters.get(obj.Instructions.get(i).fields[3]);
                    Rejisters.put(obj.Instructions.get(i).fields[1],tmp1+tmp2);
                }
                if (obj.Instructions.get(i).fields[0].equals("sub")){
                    int tmp1 = Rejisters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Rejisters.get(obj.Instructions.get(i).fields[3]);
                    Rejisters.put(obj.Instructions.get(i).fields[1],tmp1-tmp2);
                }
                if (obj.Instructions.get(i).fields[0].equals("addi")){
                    int tmp1 = Rejisters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Integer.parseInt(obj.Instructions.get(i).fields[3]);
                    Rejisters.put(obj.Instructions.get(i).fields[1],tmp1+tmp2);
                }
                if (obj.Instructions.get(i).fields[0].equals("and")){
                    int tmp1 = Rejisters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Rejisters.get(obj.Instructions.get(i).fields[3]);
                    Rejisters.put(obj.Instructions.get(i).fields[1],tmp1&tmp2);
                }
                if (obj.Instructions.get(i).fields[0].equals("or")){
                    int tmp1 = Rejisters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Rejisters.get(obj.Instructions.get(i).fields[3]);
                    Rejisters.put(obj.Instructions.get(i).fields[1],tmp1|tmp2);
                }
                if (obj.Instructions.get(i).fields[0]=="andi"){
                    int tmp1 = Rejisters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Integer.parseInt(obj.Instructions.get(i).fields[3]);
                    Rejisters.put(obj.Instructions.get(i).fields[1],tmp1&tmp2);
                }
                if (obj.Instructions.get(i).fields[0].equals("ori")){
                    int tmp1 = Rejisters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Integer.parseInt(obj.Instructions.get(i).fields[3]);
                    Rejisters.put(obj.Instructions.get(i).fields[1],tmp1|tmp2);
                }
                if (obj.Instructions.get(i).fields[0].equals("sll")){
                    int tmp1 = Rejisters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Integer.parseInt(obj.Instructions.get(i).fields[3]);
                    Rejisters.put(obj.Instructions.get(i).fields[1],tmp1<<tmp2);
                }
                if (obj.Instructions.get(i).fields[0].equals("slt")){
                    int tmp1 = Rejisters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Rejisters.get(obj.Instructions.get(i).fields[3]);
                    if (tmp1<tmp2)
                        Rejisters.put(obj.Instructions.get(i).fields[1],1);
                    else
                        Rejisters.put(obj.Instructions.get(i).fields[1],0);
                }
                if (obj.Instructions.get(i).fields[0].equals("slti")){
                    int tmp1 = Rejisters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Integer.parseInt(obj.Instructions.get(i).fields[3]);
                    if (tmp1<tmp2)
                        Rejisters.put(obj.Instructions.get(i).fields[1],1);
                    else
                        Rejisters.put(obj.Instructions.get(i).fields[1],0);
                }
                ins.add(obj.Instructions.get(i));
            }

        }
        return ins;
    }

    public void clearRejisters(){
        Rejisters.clear();
        Rejisters.put("$0",   0);   Rejisters.put("$at", 0  );   Rejisters.put("$v0", 0);   Rejisters.put("$v1", 0);    Rejisters.put("$a0", 0);
        Rejisters.put("$a1",  0);   Rejisters.put("$a2", 0  );   Rejisters.put("$a3", 0);   Rejisters.put("$t0", 0);    Rejisters.put("$t1", 0);
        Rejisters.put("$t2",  0);   Rejisters.put("$t3", 0  );   Rejisters.put("$t4", 0 );  Rejisters.put("$t5", 0 );   Rejisters.put("$t6", 0);
        Rejisters.put("$t7",  0);   Rejisters.put("$s0", 0  );   Rejisters.put("$s1", 0 );  Rejisters.put("$s2", 0 );   Rejisters.put("$s3", 0);
        Rejisters.put("$s4",  0);   Rejisters.put("$s5", 0  );   Rejisters.put("$s6", 0 );  Rejisters.put("$s7", 0 );   Rejisters.put("$t8", 0);
        Rejisters.put("$t9",  0);   Rejisters.put("$k0", 0  );   Rejisters.put("$k1", 0 );  Rejisters.put("$gp", 0 );   Rejisters.put("$sp", 0);
        Rejisters.put("$fp",  0);   Rejisters.put("$ra", 0  );
    }
    
    public void runInstruction (Assembler.MachineSet ins){
        switch(ins.fields[0]){
            case "lw":      /*TODO*/
                break;
            case "sw":      /*TODO*/
                break;
            case "add":     //add two Rejisters
                Rejisters.replace(ins.fields[1], Rejisters.get(ins.fields[2])+ Rejisters.get(ins.fields[3]));
                break;
            case "addi":    //add rejester to number
                Rejisters.replace(ins.fields[1], Rejisters.get(ins.fields[2])+ Integer.parseInt(ins.fields[3]));
                break;
            case "sub":     //subtruct two Rejisters
                Rejisters.replace(ins.fields[1], Rejisters.get(ins.fields[2])- Integer.parseInt(ins.fields[3]));
                break;
            case "and":     //and two Rejisters
                Rejisters.replace(ins.fields[1], Rejisters.get(ins.fields[2])& Rejisters.get(ins.fields[3]));
                break;
            case "or":      //or two Rejisters
                Rejisters.replace(ins.fields[1], Rejisters.get(ins.fields[2])| Rejisters.get(ins.fields[3]));
                break;
            case "andi":    //and rejister with value
                Rejisters.replace(ins.fields[1], Rejisters.get(ins.fields[2])& Integer.parseInt(ins.fields[3]));
                break;
            case "ori":     //or rejister with value
                Rejisters.replace(ins.fields[1], Rejisters.get(ins.fields[2])| Integer.parseInt(ins.fields[3]));
                break;
            case "sll":     //Shift left logical "<<"
                Rejisters.replace(ins.fields[1], Rejisters.get(ins.fields[2])<< Rejisters.get(ins.fields[3]));
                break;
            case "slt":     //Set on less than
                Rejisters.replace(ins.fields[1], Rejisters.get(ins.fields[2])< Rejisters.get(ins.fields[3]) ? 1 : 0);
                break;
            case "slti":    //Set on less than immediate
                Rejisters.replace(ins.fields[1], Rejisters.get(ins.fields[2])< Integer.parseInt(ins.fields[3]) ? 1 : 0);
                break;
            case "lui":     /*TODO*/
                break;
        }
    }
}
