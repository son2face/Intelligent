var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
import { Injectable } from '@angular/core';
var ConfirmationPopoverOptions = (function () {
    function ConfirmationPopoverOptions() {
        this.confirmText = 'Confirm';
        this.cancelText = 'Cancel';
        this.confirmButtonType = 'success';
        this.cancelButtonType = 'default';
        this.placement = 'top';
        this.hideConfirmButton = false;
        this.hideCancelButton = false;
        this.popoverClass = '';
        this.appendToBody = false;
    }
    return ConfirmationPopoverOptions;
}());
export { ConfirmationPopoverOptions };
/**
 * @private
 */
var ConfirmationPopoverWindowOptions = (function (_super) {
    __extends(ConfirmationPopoverWindowOptions, _super);
    function ConfirmationPopoverWindowOptions() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    ConfirmationPopoverWindowOptions.decorators = [
        { type: Injectable },
    ];
    /** @nocollapse */
    ConfirmationPopoverWindowOptions.ctorParameters = function () { return []; };
    return ConfirmationPopoverWindowOptions;
}(ConfirmationPopoverOptions));
export { ConfirmationPopoverWindowOptions };
//# sourceMappingURL=confirmationPopoverOptions.provider.js.map