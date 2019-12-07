<!-- section 1 -->
<div class="navigation-text" id="navigation-text">
    Comment page caption
</div>
<div class="mdl-grid main-layout">
    <div class="mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">
         <div id="error-container-1">
             <span class="mdl-chip" id="error-message-1">
             <span class="mdl-chip__text" id="error-message-content-1"></span>
             </span>
         </div>
    </div>
    <!-- create type -->
    <div id="form-create"
         class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
        <form action="#">
            <div class="mdl-grid">
                <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" id="commodityTypeName">
                    <label class="mdl-textfield__label" for="commodityTypeName">Commodity Type Name...</label>
                </div>

                <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield">
                    <textarea class="mdl-textfield__input" type="text" rows="3" id="commodityTypeDesc"></textarea>
                    <label class="mdl-textfield__label" for="commodityTypeDesc">Description...</label>
                </div>
                <div class="mdl-cell mdl-cell--12-col">
                    <!-- Accent-colored raised button with ripple -->
                    <button id="btn-createtype"
                            class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent"
                            type="button">
                        Create type
                    </button>
                </div>
            </div>
        </form>
        <!-- form edit commodityType -->
    </div>
    <div id="form-edit-type"
         class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
        <form action="#">
            <div class="mdl-grid">
                <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label is-upgraded is-dirty"
                     data-upgraded=",MaterialTextfield">
                    <input class="mdl-textfield__input" type="text" id="commodityTypeNameEdit">
                    <label class="mdl-textfield__label" for="commodityTypeNameEdit">Commodity Type Name...</label>
                </div>
                <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield  is-upgraded is-dirty"
                     data-upgraded=",MaterialTextfield">
                        <textarea class="mdl-textfield__input" type="text" rows="3"
                                  id="commodityTypeDescEdit"></textarea>
                    <label class="mdl-textfield__label" for="commodityTypeDescEdit">Description...</label>
                </div>
                <div class="mdl-cell mdl-cell--12-col">
                    <!-- Accent-colored raised button with ripple -->
                    <button id="btn-edit-type"
                            class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent"
                            type="button">
                        Edit type
                    </button>
                </div>
            </div>
        </form>
    </div>
    <!-- end form edit commodityType -->

    <div id="type-list" class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
        <!-- commodityType list -->

        <table class="mdl-data-table mdl-js-data-table" style="width:100%">
            <thead>
            <tr>
                <th class="mdl-data-table__cell--non-numeric">Name</th>
                <th class="mdl-data-table__cell--non-numeric">Setup</th>
                <th class="mdl-data-table__cell--non-numeric">Delete</th>
            </tr>
            </thead>
            <tbody id="container-types">


            </tbody>
        </table>
        <!-- // end commodityType list -->
    </div>

    <!-- drawAddAttributes -->
    <div id="add-attributes-container" class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
        <div class="mdl-grid">
            <div class="mdl-cell mdl-cell--12-col mdl-card__supporting-text no-padding">
                <h6>Create constant attributes of you commodity type</h6>
                Example:<br/> The wear's attribute name is <b>Size</b> and attribute value for <b>Size</b> is
                <b>S</b>
                <br/><b>Measure</b> can be undefined/empty.
            </div>

            <div class="mdl-cell mdl-cell--12-col no-padding">
                <h6>Select attribute data type:</h6>
                <div id="data-type-container" style="white-space: nowrap;">
                    <span class="mdl-list__item-primary-content">String</span>
                    <span class="mdl-list__item-secondary-action">
                        <label class="demo-list-radio mdl-radio mdl-js-radio mdl-js-ripple-effect"
                               for="list-option-1">
                        <input type="radio" id="list-option-1" class="mdl-radio__button" name="dataType"
                               value="String" checked/>
                        </label>
                    </span>
                </div>
            </div>

            <div class="mdl-cell mdl-cell--12-col">

                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" id="propertyName">
                    <label class="mdl-textfield__label" for="propertyName">Attribute Name...</label>
                </div>
            </div>
            <div class="mdl-cell mdl-cell--12-col">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" id="attributeValue">
                    <label class="mdl-textfield__label" for="attributeValue">Attribute Value...</label>
                </div>
            </div>
            <div class="mdl-cell mdl-cell--12-col">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" id="attributeMeasure">
                    <label class="mdl-textfield__label" for="attributeMeasure">Measure (can be ampty)...</label>
                </div>
            </div>
            <div class="mdl-cell mdl-cell--12-col">
                <button id="btn-create-attribute"
                        class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                    Add
                </button>
            </div>
        </div>
    </div>
    <!-- // drawAddAttributes -->
    <div id="attributes-container" >
        <div class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
            <table class="mdl-data-table mdl-js-data-table" style="width:100%">
                <thead>
                <tr>
                    <th class="mdl-data-table__cell--non-numeric">Attr. Name: Value</th>
                    <th class="mdl-data-table__cell--non-numeric">Settings</th>
                    <th class="mdl-data-table__cell--non-numeric">Delete</th>
                </tr>
                </thead>
                <tbody id="container-properties">
                <td class="mdl-data-table__cell--non-numeric">size: s</td>
                <td class="mdl-data-table__cell--non-numeric">type: String<br/>Measure: NAN</td>
                <td class="mdl-data-table__cell--non-numeric">
                    DELETE
                </td>
                </tbody>
            </table>
        </div>
    </div>

</div>

<button id="draw-create-type-btn" onclick="drawCreateTypeBtnAction();" class="draw-create-type-btn mdl-shadow--2dp mdl-button mdl-js-button mdl-button--fab mdl-js-ripple-effect mdl-button--colored">
      <i class="material-icons">add</i>
</button>

<div id="btn-attribute-back-container" class="left-action-container-buttons">
    <button  id="btn-attribute-back" onclick="btnAttributeBackAction();"  class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
        <i class="material-icons">arrow_back</i>
    </button>
</div>

<div id="add-attributes-container-btns" class="left-action-container-buttons">
      <button onclick="setupType();" "btn-attribute-back" class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
          <i class="material-icons">arrow_back</i>
      </button>
</div>

<div id="attributes-container-btns" class="left-action-container-buttons">
    <button onclick="drawTypeList();" "btn-attribute-back" class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
        <i class="material-icons">arrow_back</i>
    </button>&nbsp;
    <button onclick="drawAddAttributes();" class="mdl-shadow--2dp mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
      <i class="material-icons">add</i>
    </button>
</div>


<div id="form-edit-type-btns" class="left-action-container-buttons">
      <button onclick="drawTypeList();" "btn-attribute-back" class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
          <i class="material-icons">arrow_back</i>
      </button>
</div>

<div id="form-create-btns" class="left-action-container-buttons">
<button onclick="drawTypeList();" class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
  <i class="material-icons">arrow_back</i>
</button>
</div>