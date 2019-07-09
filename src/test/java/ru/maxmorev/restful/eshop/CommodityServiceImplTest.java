package ru.maxmorev.restful.eshop;


import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import ru.maxmorev.restful.eshop.annotation.DataSets;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.rest.request.RequestAttributeValue;
import ru.maxmorev.restful.eshop.rest.request.RequestCommodity;
import ru.maxmorev.restful.eshop.entities.*;
import ru.maxmorev.restful.eshop.services.CommodityService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by maxim.morev on 05/01/19.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceTestConfig.class, ServiceConfig.class})

@TestExecutionListeners({ServiceTestExecutionListener.class})
@ActiveProfiles("test")
public class CommodityServiceImplTest  extends AbstractTransactionalJUnit4SpringContextTests {
//AbstractJdbcOperationsSessionRepositoryITests

    private CommodityService commodityService;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @PersistenceContext
    private EntityManager em;

    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    @Test
    public void testFindAllTypes() throws Exception {
        List<CommodityType> typeList = commodityService.findAllTypes();

        assertNotNull(typeList);
        assertEquals(1, typeList.size());
    }

    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    @Test
    public void testFindTypeById() throws Exception {
        CommodityType result = commodityService.findTypeById(1L);
        assertNotNull(result);
    }

    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    @Test
    public void testFindPropertiesByTypeId() throws Exception {
        List<CommodityAttribute> properties = commodityService.findPropertiesByTypeId(1L);
        assertNotNull(properties);
        assertEquals(2, properties.size());
    }

    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    @Test(expected=javax.persistence.PersistenceException.class)
    public void testDeletePropertyValueByIdError() throws Exception {
        //deletePropertyValueById(Long valueId)
        List<CommodityAttribute> properties = commodityService.findPropertiesByTypeId(1L);
        logger.info(properties);
        commodityService.deletePropertyValueById(3l);
        em.flush();
        /**
         * the method deletePropertyValueById will try to delete property witch id used as FK in TABLE commodity_branch_property_set
         */
        properties = commodityService.findPropertiesByTypeId(1L);
        logger.info(properties);
        assertEquals(0, properties.size());

    }

    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    public void testDeletePropertyValueById() throws Exception {
        //deletePropertyValueById(Long valueId)
        List<CommodityAttribute> properties = commodityService.findPropertiesByTypeId(1L);
        assertEquals(2, properties.size());

        commodityService.deletePropertyValueById(9l);
        em.flush();
        /**
         * the method deletePropertyValueById also delete property without value
         */
        properties = commodityService.findPropertiesByTypeId(1L);
        assertEquals(1, properties.size());

    }

    @Test
    public void testAddProperty() throws  Exception {
        //void addProperty(RequestAttributeValue property);
        RequestAttributeValue requestPV = new RequestAttributeValue();
        requestPV.setName("color");
        requestPV.setTypeId(1L);
        requestPV.setValue("ffffff");
        commodityService.addProperty(requestPV);
        em.flush();
        List<CommodityAttribute> properties = commodityService.findPropertiesByTypeId(1L);

        assertEquals(2, properties.size());

    }

    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    @Test
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
        rc.setImages(Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/0/06/Coffee_Beans_Photographed_in_Macro.jpg"));
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
    public void testAddType() throws Exception {
        CommodityType type = new CommodityType();
        type.setName("TestType");
        type.setDescription("Type description");

        commodityService.addType(type);

        em.flush();

        List<CommodityType> types = commodityService.findAllTypes();
        assertEquals(1, types.size());

    }


    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    @Test
    public void testDeleteTypeById() throws Exception {

        commodityService.deleteTypeById(1L);
        em.flush();

        //Optional<CommodityType> result = commodityService.findTypeById(1L);
        List<CommodityType> types = commodityService.findAllTypes();
        assertEquals(0, types.size() );
    }

    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    @Test
    public void testFinaAllBranches() throws Exception {
        List<CommodityBranch> branches = commodityService.findAllBranches();
        assertEquals(1, branches.size() );
    }

    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    @Test
    public void testFindBranchById() throws Exception {
        CommodityBranch branch = commodityService.findBranchById(5L);
        assertNotNull(branch);
    }

    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    @Test
    public void testUpdateCommodity() throws Exception {
        //updateCommodity(RequestCommodity requestCommodity )
        RequestCommodity rc = new RequestCommodity();
        CommodityBranch branch = commodityService.findBranchById(5L);
        assertNotNull(branch);
        List<String> images = new ArrayList<>();
        for(CommodityImage image: branch.getCommodity().getImages()){
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

        CommodityBranch branchUpdate = commodityService.findBranchById(5L);
        assertEquals((long)rc.getAmount(), (long)branchUpdate.getAmount() );
        assertEquals(rc.getPrice(), branchUpdate.getPrice(), 2);
        assertEquals(rc.getName(), branchUpdate.getCommodity().getName());
        List<CommodityBranchAttributeSet> list = Lists.newArrayList(branch.getAttributeSet());
        assertEquals((long)9, (long)list.get(0).getAttributeValue().getId());

    }

    //findAllCommodities
    @DataSets(setUpDataSet= "/ru/maxmorev/restful/eshop/CommodityServiceImplTest.xls")
    @Test
    public void testFinaAllCommodities() throws Exception {
        List<Commodity> commodities = commodityService.findAllCommodities();
        assertEquals(1, commodities.size() );
        logger.info("commodities " + commodities);
    }

}
