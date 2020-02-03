<!-- section 3 -->
<div class="navigation-text" id="navigation-text-3">
    Comment page caption
</div>
<div class="mdl-grid main-layout">
    <div class="mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">
         <div id="error-container-3">
             <span class="mdl-chip" id="error-message-3">
             <span class="mdl-chip__text" id="error-message-content-3"></span>
             </span>
         </div>
    </div>

    <!-- list orders -->
    <div id="customer-orders"
         class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
        <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp" style="width:100%">
            <thead>
            <tr>
                <th class="mdl-data-table__cell--non-numeric">OrderId/Date</th>
                <th class="mdl-data-table__cell--non-numeric">Order Info</th>
            </tr>
            </thead>
            <tbody id="container-orders">

            </tbody>
        </table>
    </div>

    <!-- order -->
    <div id="customer-order"
         class="mdl-shadow--2dp mdl-cell mdl-cell--12-col-phone mdl-cell--12-col mdl-cell--6-col-tablet mdl-cell--6-col-desktop">
        <div class="mdl-grid" id="container-purchases">

        </div>
        <!-- Accent-colored raised button with ripple -->
        <button id="order-action" onclick="orderAction(this);" class="mdl-cell mdl-cell--12 mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">
          Button
        </button>
    </div>

</div>

<div id="btn-order-back" class="left-action-container-buttons">
    <button  id="btn-attribute-back" onclick="btnOrderBackAction();"  class="mdl-button mdl-js-button mdl-button--fab mdl-button--mini-fab">
        <i class="material-icons">arrow_back</i>
    </button>
</div>