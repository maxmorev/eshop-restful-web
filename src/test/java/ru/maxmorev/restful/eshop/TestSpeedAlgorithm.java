package ru.maxmorev.restful.eshop;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.maxmorev.restful.eshop.annotation.AttributeDataType;

import java.util.*;
//Algorithm

public class TestSpeedAlgorithm {

    private final static Logger logger = LoggerFactory.getLogger(TestSpeedAlgorithm.class);

    public static void main(String... args){
        String rndStr = RandomStringUtils.randomAlphabetic(5);
        System.out.println(rndStr);

        String auth = "CUSTOMER";
        System.out.println( "Auth: "+  auth.split(",").length + auth.split(",") );

        /*Random random = new Random();
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

        }*/

    }

}
