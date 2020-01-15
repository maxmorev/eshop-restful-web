package ru.maxmorev.restful.eshop.services;


import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.AttributeDataType;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.entities.*;
import ru.maxmorev.restful.eshop.rest.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.rest.request.RequestCommodity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by maxim.morev on 11/08/19.
 */
@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringJUnitConfig(classes = {ServiceTestConfig.class, ServiceConfig.class})
@DisplayName("Integration CommodityService Test")
public class CommodityServiceTest {

    @Autowired
    private CommodityService commodityService;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("should return all types")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void findAllTypes() {
        List<CommodityType> types = commodityService.findAllTypes();
        assertNotNull(types);
        assertEquals(1, types.size());
    }

    @Test
    @DisplayName("should add type")
    @Transactional
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testAddType() throws Exception {
        CommodityType type = new CommodityType();
        type.setName("TestType");
        type.setDescription("Type description");

        commodityService.addType(type);

        em.flush();

        List<CommodityType> types = commodityService.findAllTypes();
        assertEquals(1, types.size());

    }

    @Test
    @DisplayName("should delete type")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testDeleteTypeById() throws Exception {

        commodityService.deleteTypeById(1L);
        em.flush();
        List<CommodityType> types = commodityService.findAllTypes();
        assertTrue(types.isEmpty());
    }

    @Test
    @DisplayName("should return type by ID")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFindTypeById() throws Exception {
        Optional<CommodityType> result = commodityService.findTypeById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("should return 2 attributes by type 1")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFindAttributeByTypeId() throws Exception {
        List<CommodityAttribute> properties = commodityService.findAttributesByTypeId(1L);
        assertEquals(2, properties.size());
    }

    @Test
    @DisplayName("should remove attribute without value")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testDeleteAttributeValueById() throws Exception {
        List<CommodityAttribute> properties = commodityService.findAttributesByTypeId(1L);
        assertEquals(2, properties.size());

        commodityService.deleteAttributeValueById(9l);
        em.flush();
        /**
         * the method deleteAttributeValueById also delete property without value
         */
        properties = commodityService.findAttributesByTypeId(1L);
        assertEquals(1, properties.size());

    }

    @Test
    @DisplayName("should add attribute for type id=1")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testAddAttribute() throws Exception {
        //void addProperty(RequestAttributeValue property);
        RequestAttributeValue requestPV = new RequestAttributeValue();
        requestPV.setName("color");
        requestPV.setTypeId(1L);
        requestPV.setValue("#ffffff");
        commodityService.addAttribute(requestPV);
        em.flush();
        List<CommodityAttribute> properties = commodityService.findAttributesByTypeId(1L);

        assertEquals(2, properties.size());

    }

    @Test
    @DisplayName("should throw exception while remove attribute value which is in branch")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testDeleteAttributeValueByIdError() throws Exception {
        //deletePropertyValueById(Long valueId)
        log.info("BEFORE OPERATION");
        List<CommodityBranch> branches = commodityService.findAllBranches();
        assertTrue(branches.size() == 1);

        CommodityBranch branch = branches.get(0);
        log.info("branch.getAttributeSet().size()={}", branch.getAttributeSet().size());
        assertTrue(branch.getAttributeSet().size() == 1);

        branch.getAttributeSet().forEach(as -> {
            log.info("attribute value is {}", as.getAttributeValue());
            assertTrue(as.getAttributeValue().getId() == 3l);
        });

        commodityService.deleteAttributeValueById(3l);

        /**
         * the method deletePropertyValueById will try to delete property witch id used as FK in TABLE commodity_branch_property_set
         */
        assertThrows(javax.persistence.PersistenceException.class, em::flush);

        branches = commodityService.findAllBranches();
        assertTrue(branches.size() == 1);
        branch = branches.get(0);
        log.info("branch.getAttributeSet().size()={}", branch.getAttributeSet().size());
        assertTrue(branch.getAttributeSet().size() == 1);
        branch.getAttributeSet().forEach(as -> {
            log.info("attribute value is {}", as.getAttributeValue());
            assertTrue(as.getAttributeValue().getId() == 3l);
        });
    }

    @Test
    @DisplayName("should add commodity")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testAddCommodity() throws Exception {

        //void addCommodity(RequestCommodity requestCommodity);
        RequestCommodity rc = new RequestCommodity();
        rc.setAmount(1);
        rc.setPrice(3500f);
        rc.setName("Commodity1");
        rc.setOverview("Overview size <= 2048");
        rc.setShortDescription("Short Description");
        rc.setTypeId(1L);
        rc.setPropertyValues(Arrays.asList(3L));
        rc.setImages(ImmutableList.of("https://upload.wikimedia.org/wikipedia/commons/0/06/Coffee_Beans_Photographed_in_Macro.jpg"));
        rc.setCurrencyCode("EUR");
        commodityService.addCommodity(rc);
        em.flush();

        List<Commodity> commodities = commodityService.findAllCommodities();
        assertEquals(2, commodities.size());

        List<CommodityBranch> branches = commodityService.findAllBranches();
        assertEquals(2, branches.size());

        Commodity cm = commodities.get(0);
        assertEquals(1, cm.getImages().size());

    }

    @Test
    @DisplayName("should find all commodities")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFinaAllCommodities() throws Exception {
        List<Commodity> commodities = commodityService.findAllCommodities();
        assertEquals(1, commodities.size());
        log.info("commodities {}", commodities);
    }

    @Test
    @DisplayName("should update commodity")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testUpdateCommodity() throws Exception {
        //updateCommodity(RequestCommodity requestCommodity )
        RequestCommodity rc = new RequestCommodity();
        CommodityBranch branch = commodityService.findBranchById(5L).get();
        assertNotNull(branch);
        List<String> images = new ArrayList<>();
        for (CommodityImage image : branch.getCommodity().getImages()) {
            images.add(image.getUri());
        }
        rc.setImages(images);
        //update propertyValueId
        List<Long> values = Arrays.asList(9L);
        rc.setPropertyValues(values);
        rc.setTypeId(branch.getCommodity().getType().getId());
        rc.setShortDescription(branch.getCommodity().getShortDescription());
        rc.setOverview(branch.getCommodity().getOverview());
        rc.setName("BOMBER MA-1");//update name was t-shirt
        rc.setAmount(3);//was 5
        rc.setPrice(9000F);//was 3500f
        rc.setBranchId(branch.getId());//if present - update in controller
        commodityService.updateCommodity(rc);
        em.flush();

        CommodityBranch branchUpdate = commodityService.findBranchById(5L).get();
        assertEquals((long) rc.getAmount(), (long) branchUpdate.getAmount());
        assertEquals(rc.getPrice(), branchUpdate.getPrice(), 2);
        assertEquals(rc.getName(), branchUpdate.getCommodity().getName());
        List<CommodityBranchAttributeSet> list = Lists.newArrayList(branch.getAttributeSet());
        assertEquals((long) 9, (long) list.get(0).getAttributeValue().getId());

    }

    @Test
    @DisplayName("should find commodity by Type name")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFinaCommoditiesByTypeName() throws Exception {
        List<Commodity> commodityList = commodityService.findAllCommoditiesByTypeName("TypeTest");
        assertTrue(commodityList.size() > 0);
    }

    @Test
    @DisplayName("should find commodity by ID")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFinaCommodityById() throws Exception {
        Optional<Commodity> commodity = commodityService.findCommodityById(4L);
        assertTrue(commodity.isPresent());
    }

    @Test
    @DisplayName("should return all branches")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void findAllBranches() {
        List<CommodityBranch> branches = commodityService.findAllBranches();
        assertTrue(branches.size() == 1);
    }

    @Test
    @DisplayName("should find branche by ID")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFindBranchById() throws Exception {
        CommodityBranch branch = commodityService.findBranchById(5L).get();
        assertNotNull(branch);
    }

    @Test
    @DisplayName("should check Availeble Attribute Data Types")
    public void testGetAvailebleAttributeDataTypes() {

        assertEquals(AttributeDataType.values().length,
                commodityService.getAvailebleAttributeDataTypes().size());
    }


}
