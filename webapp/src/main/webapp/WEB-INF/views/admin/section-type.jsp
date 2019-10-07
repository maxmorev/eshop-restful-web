<!-- section 1 -->
<section class="mdl-layout__tab-panel is-active" id="scroll-tab-1">
            <div class="page-content">
                <!-- Your content goes here -->
                <div class="mdl-grid">
                    <!-- left form -->
                    <!-- Basic Chip for errors -->
                    <div class="mdl-cell mdl-cell--2-col">
                        <!-- Mini FAB button -->
                        <button id="btn-attribute-back" class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
                            <i class="material-icons">arrow_back</i>
                        </button>
                    </div>
                    <div class="mdl-cell mdl-cell--8-col">
                        <div id="error-container-1">
                        <span class="mdl-chip" id="error-message-1">
                        <span class="mdl-chip__text" id="error-message-content-1"></span>
                        </span>
                        </div>
                    </div>
                    <div class="mdl-cell mdl-cell--2-col" id="navigation-text"></div>
                    <!-- form create commodityType -->
                </div>
                <div class="mdl-grid">
                    <div class="mdl-cell mdl-cell--2-col">
                        <div id="form-create">
                        <form  action="#">

                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" type="text" id="commodityTypeName">
                                <label class="mdl-textfield__label" for="commodityTypeName">Commodity Type Name...</label>
                            </div>
                            <div class="mdl-textfield mdl-js-textfield">
                                <textarea class="mdl-textfield__input" type="text" rows= "3" id="commodityTypeDesc" ></textarea>
                                <label class="mdl-textfield__label" for="commodityTypeDesc">Description...</label>
                            </div>
                            <br/>
                            <!-- Accent-colored raised button with ripple -->
                            <button id="btn-createtype" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent"  type="button">
                                Create type
                            </button>
                        </form>
                        </div>
                        <!-- end: form create commodityType -->
                        <!-- form edit commodityType -->
                        <div id="form-edit-type">
                            <form  action="#">
                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label is-upgraded is-dirty" data-upgraded=",MaterialTextfield">
                                    <input class="mdl-textfield__input" type="text" id="commodityTypeNameEdit">
                                    <label class="mdl-textfield__label" for="commodityTypeNameEdit">Commodity Type Name...</label>
                                </div>
                                <div class="mdl-textfield mdl-js-textfield  is-upgraded is-dirty" data-upgraded=",MaterialTextfield">
                                    <textarea class="mdl-textfield__input" type="text" rows= "3" id="commodityTypeDescEdit" ></textarea>
                                    <label class="mdl-textfield__label" for="commodityTypeDescEdit">Description...</label>
                                </div>
                                <br/>
                                <!-- Accent-colored raised button with ripple -->
                                <button id="btn-edit-type" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent"  type="button">
                                    Edit type
                                </button>
                            </form>
                        </div>
                        <!-- end form edit commodityType -->

                    </div>
                    <!-- right content -->
                    <div class="mdl-cell mdl-cell--6-col">
                    <!-- commodityType list -->
                    <div id="type-list">

                        <table  class="mdl-data-table mdl-js-data-table">
                            <thead>
                            <tr>
                                <th class="mdl-data-table__cell--non-numeric">id</th>
                                <th>Name</th>
                                <th>Commodity quantity</th>
                                <th>Delete</th>
                            </tr>
                            </thead>
                            <tbody id="container-types">


                            </tbody>
                        </table>
                       </div>
                    <!-- // end commodityType list -->
                    <!-- properties -->
                    <div id="type-properties">
                            <div ><h6>Create constant attributes of you commodity type</h6>
                                Example:<br/> The wear's attribute name is <b>Size</b> and attribute value for <b>Size</b> is <b>S</b>
                                <br/><b>Measure</b> can be undefined.
                            </div>

                            <div class="mdl-grid">

                                <div  class="mdl-cell mdl-cell--12-col">
                                    Select attribute data type:
                                    <div class="mdl-grid" id="data-type-container">
                                        <div mdl-cell mdl-cell--2-col>
                                            <span class="mdl-list__item-primary-content">String</span>
                                            <span class="mdl-list__item-secondary-action">
                                                <label class="demo-list-radio mdl-radio mdl-js-radio mdl-js-ripple-effect" for="list-option-1">
                                                <input type="radio" id="list-option-1" class="mdl-radio__button" name="dataType" value="String" checked />
                                                </label>
                                            </span>
                                        </div>

                                    </div>
                                </div>

                                <div class="mdl-cell mdl-cell--1-col">

                                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                        <input class="mdl-textfield__input" type="text" id="propertyName">
                                        <label class="mdl-textfield__label" for="propertyName">Name...</label>
                                    </div>
                                </div>
                                <div class="mdl-cell mdl-cell--1-col">
                                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                        <input class="mdl-textfield__input" type="text" id="attributeValue">
                                        <label class="mdl-textfield__label" for="attributeValue">Value...</label>
                                    </div>
                                </div>
                                <div class="mdl-cell mdl-cell--1-col">
                                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                        <input class="mdl-textfield__input" type="text" id="attributeMeasure">
                                        <label class="mdl-textfield__label" for="attributeMeasure">Measure...</label>
                                    </div>
                                </div>
                                <div class="mdl-cell mdl-cell--1-col">
                                    <!-- Colored FAB button with ripple -->
                                    <button id="btn-create-attribute" class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored">
                                        <i class="material-icons">add</i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    <!-- // properties -->
                    </div>
                    <div class="mdl-cell mdl-cell--4-col" id="attributes-container">
                        <table  class="mdl-data-table mdl-js-data-table">
                            <thead>
                            <tr>
                                <th class="mdl-data-table__cell--non-numeric">Attribute Name</th>
                                <th class="mdl-data-table__cell--non-numeric">Value</th>
                                <th class="mdl-data-table__cell--non-numeric">Data Type</th>
                                <th class="mdl-data-table__cell--non-numeric">Measure</th>
                                <th class="mdl-data-table__cell--non-numeric">Delete attribute</th>
                            </tr>
                            </thead>
                            <tbody id="container-properties">
                            <td class="mdl-data-table__cell--non-numeric">size</td>
                            <td class="mdl-data-table__cell--non-numeric">S</td>
                            <td class="mdl-data-table__cell--non-numeric">String</td>
                            <td class="mdl-data-table__cell--non-numeric"></td>
                            <td class="mdl-data-table__cell--non-numeric">
                                DELETE
                            </td>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </section>
<!-- end of section 1 -->