package ru.maxmorev.restful.eshop;

import java.util.*;
//Algorithm

public class TestSpeedAlgorithm {

    public static void main(String... args){

        Random random = new Random();
        List<Long> values = new ArrayList<Long>(10000000);
        values.forEach(v->values.add(random.nextLong()));
        List<Long> branch = new ArrayList<>(10000000);
        values.forEach(v->branch.add(v));
        boolean eq;
        long start = new Date().getTime();
        Collections.sort(values);
        if(values.size()==branch.size()){
            //check values
            Set<Long> sortedValsSet = new HashSet<>();//u can try to set TreeSet instead HashSet
            branch.forEach(propertySet->sortedValsSet.add(propertySet));
            eq = true;
            for(Long v:values){
                if(sortedValsSet.add(v)){
                    eq = false;
                    break;
                }
            }
            if(eq){ //there is a branch with identical set of attributes values
                System.out.println("there is a branch with identical set of attributes values");
                System.out.println(new Date().getTime()-start);
            }

        }

    }

}
