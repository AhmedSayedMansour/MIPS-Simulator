import java.util.ArrayList;
import java.util.HashMap;

public class Simulator {
    Assembler AssemblerObject ;
    HashMap<String, Integer> Rejesters = new HashMap<>();   //rejesters and its values
    HashMap<String, Integer> Memories = new HashMap<>();    //Used memories
    int pointer = 0 ;
    int maxPointer = AssemblerObject.Instructions.size();

    public Simulator(Assembler obj){
        AssemblerObject = obj;
        Rejesters.put("$0",   0);   Rejesters.put("$at", 0  );   Rejesters.put("$v0", 0);   Rejesters.put("$v1", 0);    Rejesters.put("$a0", 0);
        Rejesters.put("$a1",  0);   Rejesters.put("$a2", 0  );   Rejesters.put("$a3", 0);   Rejesters.put("$t0", 0);    Rejesters.put("$t1", 0);
        Rejesters.put("$t2",  0);   Rejesters.put("$t3", 0  );   Rejesters.put("$t4", 0 );  Rejesters.put("$t5", 0 );   Rejesters.put("$t6", 0);
        Rejesters.put("$t7",  0);   Rejesters.put("$s0", 0  );   Rejesters.put("$s1", 0 );  Rejesters.put("$s2", 0 );   Rejesters.put("$s3", 0);
        Rejesters.put("$s4",  0);   Rejesters.put("$s5", 0  );   Rejesters.put("$s6", 0 );  Rejesters.put("$s7", 0 );   Rejesters.put("$t8", 0);
        Rejesters.put("$t9",  0);   Rejesters.put("$k0", 0  );   Rejesters.put("$k1", 0 );  Rejesters.put("$gp", 0 );   Rejesters.put("$sp", 0);
        Rejesters.put("$fp",  0);   Rejesters.put("$ra", 0  );
    }
    public ArrayList<Assembler.MachineSet> kernel(Assembler obj){
        ArrayList<Assembler.MachineSet>ins = new ArrayList<Assembler.MachineSet>();
        for (int i=0; i<obj.Instructions.size(); i++){
            if(obj.Instructions.get(i).fields[0]=="beq"){///if beq
               int rs =  Rejesters.get(obj.Instructions.get(i).fields[1]);
               int rd =  Rejesters.get(obj.Instructions.get(i).fields[2]);
                if (rs==rd){
                    if (obj.Labels.get(obj.Instructions.get(i).fields[3])>obj.Instructions.size())break;
                    else i = obj.Labels.get(obj.Instructions.get(i).fields[3])-1;
                }
            }else if (obj.Instructions.get(i).fields[0]=="bne"){///if bne
                int rs =  Rejesters.get(obj.Instructions.get(i).fields[1]);
                int rd =  Rejesters.get(obj.Instructions.get(i).fields[2]);
                if (rs!=rd){
                    if (obj.Labels.get(obj.Instructions.get(i).fields[3])>obj.Instructions.size())break;
                    else i = obj.Labels.get(obj.Instructions.get(i).fields[3])-1;
                }
            }else if(obj.Instructions.get(i).fields[0]=="j"){
                if (obj.Labels.get(obj.Instructions.get(i).fields[1])>obj.Instructions.size())break;
                else i = obj.Labels.get(obj.Instructions.get(i).fields[1])-1;
            }
            else{
                if (obj.Instructions.get(i).fields[0]=="add"){
                    ins.add(obj.Instructions.get(i));
                    int tmp1 = Rejesters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Rejesters.get(obj.Instructions.get(i).fields[3]);
                    Rejesters.put(obj.Instructions.get(i).fields[1],tmp1+tmp2);
                }
                if (obj.Instructions.get(i).fields[0]=="sub"){
                    ins.add(obj.Instructions.get(i));
                    int tmp1 = Rejesters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Rejesters.get(obj.Instructions.get(i).fields[3]);
                    Rejesters.put(obj.Instructions.get(i).fields[1],tmp1-tmp2);
                }
                if (obj.Instructions.get(i).fields[0]=="addi"){
                    ins.add(obj.Instructions.get(i));
                    int tmp1 = Rejesters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Integer.parseInt(obj.Instructions.get(i).fields[3]);
                    Rejesters.put(obj.Instructions.get(i).fields[1],tmp1+tmp2);
                }
                if (obj.Instructions.get(i).fields[0]=="and"){
                    ins.add(obj.Instructions.get(i));
                    int tmp1 = Rejesters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Rejesters.get(obj.Instructions.get(i).fields[3]);
                    Rejesters.put(obj.Instructions.get(i).fields[1],tmp1&tmp2);
                }
                if (obj.Instructions.get(i).fields[0]=="or"){
                    ins.add(obj.Instructions.get(i));
                    int tmp1 = Rejesters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Rejesters.get(obj.Instructions.get(i).fields[3]);
                    Rejesters.put(obj.Instructions.get(i).fields[1],tmp1|tmp2);
                }
                if (obj.Instructions.get(i).fields[0]=="andi"){
                    ins.add(obj.Instructions.get(i));
                    int tmp1 = Rejesters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Integer.parseInt(obj.Instructions.get(i).fields[3]);
                    Rejesters.put(obj.Instructions.get(i).fields[1],tmp1&tmp2);
                }
                if (obj.Instructions.get(i).fields[0]=="ori"){
                    ins.add(obj.Instructions.get(i));
                    int tmp1 = Rejesters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Integer.parseInt(obj.Instructions.get(i).fields[3]);
                    Rejesters.put(obj.Instructions.get(i).fields[1],tmp1|tmp2);
                }
                if (obj.Instructions.get(i).fields[0]=="sll"){
                    ins.add(obj.Instructions.get(i));
                    int tmp1 = Rejesters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Integer.parseInt(obj.Instructions.get(i).fields[3]);
                    Rejesters.put(obj.Instructions.get(i).fields[1],tmp1<<tmp2);
                }
                if (obj.Instructions.get(i).fields[0]=="slt"){
                    ins.add(obj.Instructions.get(i));
                    int tmp1 = Rejesters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Rejesters.get(obj.Instructions.get(i).fields[3]);
                    if (tmp1<tmp2)
                    Rejesters.put(obj.Instructions.get(i).fields[1],1);
                    else
                    Rejesters.put(obj.Instructions.get(i).fields[1],0);
                }
                if (obj.Instructions.get(i).fields[0]=="slt"){
                    ins.add(obj.Instructions.get(i));
                    int tmp1 = Rejesters.get(obj.Instructions.get(i).fields[2]);
                    int tmp2 = Integer.parseInt(obj.Instructions.get(i).fields[3]);
                    if (tmp1<tmp2)
                        Rejesters.put(obj.Instructions.get(i).fields[1],1);
                    else
                        Rejesters.put(obj.Instructions.get(i).fields[1],0);
                }
                ins.add(obj.Instructions.get(i));
            }

        }
        return ins;
    }

}