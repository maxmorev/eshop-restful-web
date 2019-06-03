     <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
     <%@ page isELIgnored="false" %>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
     <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
     <%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
     <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

    <spring:url value="/commodity" var="showCommodityUrl"/>
    <spring:message code="label_price" var="labelPrice"/>
    <spring:message code="label_colors" var="labelColors"/>
    <spring:message code="label_sizes" var="labelSizes"/>

<script type="text/javascript">

function showWearAttributes(id, sizes, colors){
    var content='Sizes:';
    sizes.forEach(function(size){ content+= '<div class="textCircle">&#160;'+size+'&#160;</div>&#160;';});
    content += '<br/>';
    content += 'Colors:';
    colors.forEach(function(color){ content+= '<div class="colorCircleSml" style="background: #'+color+'">&#160;&#160;&#160;&#160;</div>&#160;';});
    $('#attribute-container-'+id).empty();
    $('#attribute-container-'+id).append(content);
}

function showAttributes(id, attributeSet){

    var content="";
    attributeSet.forEach(function(prop){
        content += prop.attribute.name + ": " + prop.attributeValue.value + " " + prop.attribute.measure + "<br/>";
    });
    $('#attribute-container-'+id).empty();
    $('#attribute-container-'+id).append(content);

}

function isWear(name){
    if( name=="size" || name=="color" ) {
        return true;
    }
    return false;
}

$(document).ready(function () {

    var commoditiesJsonStr = '${commodities}';
    var res = commoditiesJsonStr.replace(/\n/g, " ");//cleanup string
    var commodities = JSON.parse(res);


    commodities.forEach( function(commodity) {
        //process commodity
        const attributes = commodity.branches[0].attributeSet;
        var notWearAttributes = attributes.filter( function(a){return !isWear(a.attribute.name) ; });
        if(notWearAttributes.length>0){
            notWearAttributes.sort(function(a,b){ return a.attribute.measure>b.attribute.measure});
            showAttributes(commodity.id, notWearAttributes);
        }else{
            //show attributes for wear

            var colors = [];
            var sizes = [];
            commodity.branches.forEach( function(branch){

                branch.attributeSet.forEach(function(a){
                    if(a.attribute.name=="color"){
                        if( !colors.includes(a.attributeValue.value) ){
                            colors.push(a.attributeValue.value);
                        }
                    }
                    if(a.attribute.name=="size"){
                         if( !sizes.includes(a.attributeValue.value) ){
                            sizes.push(a.attributeValue.value);
                         }
                    }
                });

            });
            showWearAttributes(commodity.id, sizes, colors);
        }
    }
    );
});
</script>

    <div class="mdl-grid portfolio-max-width">
        <c:if test="${not empty commodities}">
            <c:forEach items="${commodities}" var="commodity">
                <div class="mdl-cell mdl-card mdl-shadow--4dp portfolio-card">
                    <div class="mdl-card__media">
                        <a href="${showCommodityUrl}/${commodity.id}"><img class="article-image" src="${commodity.images[0].uri}" border="0" alt="" /></a>
                    </div>
                    <div class="mdl-card__title">
                        <h2 class="mdl-card__title-text">${commodity.name}</h2>
                    </div>
                    <div class="mdl-card__supporting-text">
                        ${commodity.shortDescription}<br/>
                        <div id="attribute-container-${commodity.id}">

                        </div>

                    </div>
                    <div class="mdl-card__actions mdl-card--border" style="height:50px">
                        <div class="portfolio-list-action">
                        <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect mdl-button--accent" href="${showCommodityUrl}/${commodity.id}">${labelPrice} &#160; ${commodity.price} ₽</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:if>
    </div>
