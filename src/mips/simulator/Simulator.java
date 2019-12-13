/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mips.simulator;

import java.util.ArrayList;
import java.util.HashMap;

public class Simulator {
    Assembler AssemblerObject ;
    HashMap<String, Integer> Rejesters = new HashMap<>();   //rejesters and its values
    HashMap<String, Integer> Memories = new HashMap<>();    //Used memories
    
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
    
}
