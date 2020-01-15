<!-- section 2 commodity -->
<div class="navigation-text" id="navigation-text-2">
    Comment page caption
</div>
<div class="mdl-grid main-layout">

    <div class="mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">
        <div id="error-container-2">
             <span class="mdl-chip" id="error-message-2">
             <span class="mdl-chip__text" id="error-message-content-2"></span>
             </span>
        </div>
    </div>

    <!-- list commodities -->
    <div id="commodities-container"
         class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
        <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp" style="width:100%">
            <thead>
            <tr>
                <th class="mdl-data-table__cell--non-numeric">Type/ Properties</th>
                <th class="mdl-data-table__cell--non-numeric">Commodty Info</th>
            </tr>
            </thead>
            <tbody id="commodities">

            </tbody>
        </table>
    </div>

    <div id="create-commodity-step-1"
         class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">

        <div class="mdl-grid">

            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">
                <input class="mdl-textfield__input create-commodity-step-1" onblur="validateAddCommodityStep1();"
                       pattern="(.){8,256}" maxlength="256" type="text" id="commodityName">
                <label class="mdl-textfield__label" for="commodityName">Name</label>
                <span class="mdl-textfield__error">Product name must be between 8 and 256 characters long</span>
            </div>

            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
                <textarea class="mdl-textfield__input create-commodity-step-1" onblur="validateAddCommodityStep1();"
                          pattern="(.){16,256}" type="text" rows="3" id="commodityShortDesc"></textarea>
                <label class="mdl-textfield__label" for="commodityShortDesc">Short Description</label>
                <span class="mdl-textfield__error">Product Short Description must be between 16 and 256 characters long</span>
            </div>

            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
                <textarea class="mdl-textfield__input create-commodity-step-1" onblur="validateAddCommodityStep1();"
                          pattern="(.){64,2048}" type="text" rows="5" id="commodityOverview"></textarea>
                <label class="mdl-textfield__label" for="commodityOverview">Overview</label>
                <span class="mdl-textfield__error">Product Overview must be between 64 and 2048 characters long</span>
            </div>

            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--12-col-phone mdl-cell--6-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
                <input class="mdl-textfield__input create-commodity-step-1" onblur="validateAddCommodityStep1();"
                       type="text" pattern="[0-9]{1,3}" id="commodityAmount">
                <label class="mdl-textfield__label" for="commodityAmount">Amount of items</label>
                <span class="mdl-textfield__error">Input is not a number!</span>
            </div>

            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label mdl-cell mdl-cell--12-col-phone mdl-cell--6-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
                <input class="mdl-textfield__input create-commodity-step-1" onblur="validateAddCommodityStep1();"
                       type="text" pattern="^\d{1,5}$|^(?=\d+[.]\d+$).{3,6}$" id="commodityPrice">
                <label class="mdl-textfield__label" for="commodityAmount">Price per item in Euro</label>
                <span class="mdl-textfield__error">Input is not a number!</span>
            </div>

            <div class="mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">
                <button id="btn-create-commodity-step-1-back" onclick="loadBranchList();"
                        class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
                    <i class="material-icons">arrow_back</i>
                </button>&nbsp;

                <button id="btn-create-commodity-step-2"
                        class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                    Set Images
                </button>
            </div>
        </div>
    </div>

    <div id="create-commodity-step-2"
         class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">

        <div class="mdl-grid" id="image-container">
        </div>
        <div class="mdl-grid">
            <div class="mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">
                <button id="btn-create-commodity-step-2-back" onclick="drawAddCommodityStep1();"
                        class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
                    <i class="material-icons">arrow_back</i>
                </button>&nbsp;

                <button id="btn-create-commodity-step-3"
                        class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                    Set Attributes
                </button>

            </div>
        </div>
    </div>

    <div id="create-commodity-step-3"
         class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">

        <div class="mdl-grid">
            <div id="commodity-type-container"
                 class="mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">
                <!-- place for checkbox with commodity types -->
            </div>

            <div id="attributes-cm-container"
                 class="mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label small-form-text">
                    <input class="mdl-textfield__input" type="text" id="property-add-to-cm">
                    <label class="mdl-textfield__label" for="property-add-to-cm">Property...</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label small-form-text">
                    <input class="mdl-textfield__input" type="text" id="value-add-to-cm">
                    <label class="mdl-textfield__label" for="value-add-to-cm">Value...</label>
                </div>
                <button class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab"
                        onclick="addAttributeToCm();">
                    <i class="material-icons">add</i>
                </button>
            </div>
            <table class="mdl-data-table mdl-js-data-table attribute-table"
                   class="mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">
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

        </div>


        <div class="mdl-grid">
            <div class="mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">

                <button id="btn-create-commodity-step-2-back" onclick="drawAddCommodityStep2();"
                        class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
                    <i class="material-icons">arrow_back</i>
                </button>&nbsp;

                <button id="btn-create-commodity-final" onclick="btnClickAddCommodity();"
                        class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
                    Save
                </button>

            </div>
        </div>

    </div>




</div>

<div id="commodity-list-bottom-btn" class="draw-commodity-bottom-btn">
    <button id="navigate_before" onclick="loadBeforeListCommodity();"
            class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored mdl-shadow--2dp">
        <i class="material-icons">navigate_before</i></button>
    <button id="navigate_next" onclick="loadNextListCommodity();"
            class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored mdl-shadow--2dp">
        <i class="material-icons">navigate_next</i></button>
    <button onclick="drawAddCommodity();"
            class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab mdl-button--colored 1btn-add-commodity mdl-shadow--2dp">
        <i class="material-icons">add</i></button>
</div>