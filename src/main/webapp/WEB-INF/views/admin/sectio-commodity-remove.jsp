<!-- SECTION TAB 2 -->
        <section class="mdl-layout__tab-panel" id="scroll-tab-2">
            <div class="page-content">

                <!-- CREATE COMMODITY -->

                <div id="create-commodity">

                <div class="mdl-grid">

                    <!-- error and back button -->
                    <div class="mdl-cell mdl-cell--2-col">
                        <!-- Mini FAB button -->
                        <button onclick="loadBranchList();" id="btn-createupdate-back" class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
                            <i class="material-icons">arrow_back</i>
                        </button>
                    </div>
                    <div class="mdl-cell mdl-cell--8-col">
                        <div id="error-container-2">
                        <span class="mdl-chip" id="error-message-2">
                        <span class="mdl-chip__text" id="error-message-content-2"></span>
                        </span>
                        </div>
                    </div>

                    <div class="mdl-cell mdl-cell--4-col">
                        <!-- image grid -->
                        <div class="mdl-grid">

                            <div class="mdl-cell mdl-cell--4-col">
                                <!-- MDL Spinner Component -->
                                <div class="img-parent">
                                    <img src="../images/placeholder.png" id="img0" class="img-placeholder"/>
                                    <div id="img-spinner-0" class="mdl-spinner mdl-js-spinner is-active img-upload-spinner" ></div>
                                    <div class="header-content imgupload imgupload img-upload-btn" >
                                        <label class="input-custom-file mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored">
                                            <i class="material-icons">add</i>
                                            <input id="file0" type="file" onchange="postFile(0);">
                                        </label>
                                    </div>
                                </div>

                            </div>
                            <div class="mdl-cell mdl-cell--4-col">
                                <div class="img-parent">
                                    <img src="../images/placeholder.png" id="img1" class="img-placeholder"/>
                                    <div id="img-spinner-1" class="mdl-spinner mdl-js-spinner is-active img-upload-spinner" ></div>
                                    <div class="header-content imgupload img-upload-btn">
                                        <label class="input-custom-file mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored">
                                            <i class="material-icons">add</i>
                                            <input id="file1" type="file" onchange="postFile(1);">
                                        </label>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <div class="mdl-grid">

                            <div class="mdl-cell mdl-cell--4-col">
                                <div class="mdl-cell mdl-cell--4-col">
                                    <div class="img-parent">
                                        <img src="../images/placeholder.png" id="img2" class="img-placeholder"/>
                                        <div id="img-spinner-2" class="mdl-spinner mdl-js-spinner is-active img-upload-spinner" ></div>
                                        <div class="header-content imgupload img-upload-btn">
                                            <label class="input-custom-file mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored">
                                                <i class="material-icons">add</i>
                                                <input id="file2" type="file" onchange="postFile(2);">
                                            </label>
                                        </div>
                                    </div>

                                </div>

                            </div>
                            <div class="mdl-cell mdl-cell--4-col">

                                <div class="mdl-cell mdl-cell--4-col">
                                    <div class="img-parent">
                                        <img src="../images/placeholder.png" id="img3" class="img-placeholder"/>
                                        <div id="img-spinner-3" class="mdl-spinner mdl-js-spinner is-active img-upload-spinner" ></div>
                                        <div class="header-content imgupload img-upload-btn">
                                            <label class="input-custom-file mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored">
                                                <i class="material-icons">add</i>
                                                <input id="file3" type="file" onchange="postFile(3);">
                                            </label>
                                        </div>
                                    </div>

                                </div>


                            </div>

                        </div>
                        <!-- //image grid -->

                    </div>

                    <!-- COMMODITY FIELDS -->
                    <div class="mdl-cell mdl-cell--2-col">

                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <input class="mdl-textfield__input" type="text" id="commodityName">
                            <label class="mdl-textfield__label" for="commodityName">Commodity Name</label>
                        </div>
                        <div class="mdl-textfield mdl-js-textfield">
                            <textarea class="mdl-textfield__input" type="text" rows= "3" id="commodityShortDesc" ></textarea>
                            <label class="mdl-textfield__label" for="commodityShortDesc">Short Description</label>
                        </div>
                        <div class="mdl-textfield mdl-js-textfield">
                            <textarea class="mdl-textfield__input" type="text" rows= "5" id="commodityOverview" ></textarea>
                            <label class="mdl-textfield__label" for="commodityOverview">Overview</label>
                        </div>
                        <br/>


                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <input class="mdl-textfield__input" type="text" pattern="-?[0-9]*([0-9]+)?" id="commodityAmount">
                            <label class="mdl-textfield__label" for="commodityAmount">Amount</label>
                            <span class="mdl-textfield__error">Input is not a number!</span>
                        </div>
                        <!-- Numeric Textfield with Floating Label -->
                        <span class="mdl-chip">
                            <span class="mdl-chip__text">Price in Euro per item</span>
                        </span>
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <input class="mdl-textfield__input" type="text" pattern="-?[0-9]*(\.[0-9]+)?" id="commodityPrice">
                            <label class="mdl-textfield__label" for="commodityAmount">Price per item in Euro</label>
                            <span class="mdl-textfield__error">Input is not a number!</span>
                        </div>


                    </div>

                    <div class="mdl-cell mdl-cell--4-col">


                        <div id="commodity-type-container">
                            <!-- place for checkbox with commodity types -->

                        </div>
                        <br/>

                        <span class="mdl-chip">
                            <span class="mdl-chip__text">Properties:</span>
                        </span>
                        <br/>
                        <div id="attributes-cm-container">
                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label small-form-text">
                                <input class="mdl-textfield__input" type="text" id="property-add-to-cm">
                                <label class="mdl-textfield__label" for="property-add-to-cm">Property...</label>
                            </div>
                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label small-form-text">
                                <input class="mdl-textfield__input" type="text" id="value-add-to-cm">
                                <label class="mdl-textfield__label" for="value-add-to-cm">Value...</label>
                            </div>
                            <button class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab" onclick="addAttributeToCm();">
                                <i class="material-icons">add</i>
                            </button>
                        </div>
                        <table class="mdl-data-table mdl-js-data-table attribute-table">
                            <thead>
                            <tr>
                                <th>Appear</th>
                                <th class="mdl-data-table__cell--non-numeric">Property</th>
                                <th class="mdl-data-table__cell--non-numeric">Value</th>
                                <th class="mdl-data-table__cell--non-numeric">Measure</th>
                            </tr>
                            </thead>
                            <tbody id="commodity-properties">

                            </tbody>
                        </table>
                        <!-- Accent-colored raised button with ripple -->
                        <br/>
                        <button id="btn-add-commodity" onclick="btnClickAddCommodity();" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                            Add commodity
                        </button>
                        <button id="btn-update-commodity" onclick="btnClickUpdateCommodity();" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                            Update commodity
                        </button>

                        <div id="test-data">
                        </div>


                    </div>
                    <!-- END: COMMODITY FIELDS -->

                </div>

                </div>

                <!-- END: CREATE COMMODITY -->

                <!-- COMMODITY LIST -->

                <div id="commodities-container">



                    <div class="mdl-grid">

                        <div class="mdl-cell mdl-cell--4-col">
                        <button id="navigate_before" onclick="loadBeforeListCommodity();" class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored mdl-shadow--2dp"><i class="material-icons">navigate_before</i></button>
                        <button id="navigate_next"   onclick="loadNextListCommodity();" class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored mdl-shadow--2dp"><i class="material-icons">navigate_next</i></button>
                        <button onclick="loadCreateCommodityForm();" class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored 1btn-add-commodity mdl-shadow--2dp"><i class="material-icons">add</i></button>
                        </div>
                        <div class="mdl-cell mdl-cell--6-col">
                        &nbsp;
                        </div>
                        <div class="mdl-cell mdl-cell--2-col">
                        &nbsp;
                        </div>
                        <div class="mdl-cell mdl-cell--12-col">
                            <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp">
                                <thead>
                                <tr>
                                    <th class="mdl-data-table__header--sorted-descending">Vendor code</th>
                                    <th class="mdl-data-table__cell--non-numeric ">Commodity Name</th>
                                    <th class="mdl-data-table__cell--non-numeric">Type</th>
                                    <th class="mdl-data-table__cell--non-numeric">Properties</th>
                                    <th>Amount</th>
                                    <th>Price</th>
                                    <th>Edit</th>
                                </tr>
                                </thead>
                                <tbody id="commodities">

                                </tbody>
                            </table>
                        </div>
                    </div>



                </div>
                <!-- END: COMMODITY LIST-->

            </div>

        </section>
        <!-- END OF SECTION 2 -->