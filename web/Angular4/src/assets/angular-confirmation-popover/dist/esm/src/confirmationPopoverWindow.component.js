import { Component, } from '@angular/core';
import { ConfirmationPopoverWindowOptions } from './confirmationPopoverOptions.provider';
/**
 * @private
 */
var ConfirmationPopoverWindow = (function () {
    function ConfirmationPopoverWindow(options) {
        this.options = options;
    }
    ConfirmationPopoverWindow.prototype.ngAfterViewInit = function () {
        this.options.onAfterViewInit();
    };
    ConfirmationPopoverWindow.decorators = [
        { type: Component, args: [{
                    styles: ["\n    .popover {\n      display: block;\n    }\n    .bs-popover-top .arrow, .bs-popover-bottom .arrow {\n      left: 50%;\n    }\n    .bs-popover-left .arrow, .bs-popover-right .arrow {\n      top: 50%;\n    }\n    .btn {\n      transition: none;\n    }\n  "],
                    template: "\n    <ng-template #defaultTemplate let-options=\"options\">\n      <div [ngClass]=\"[\n        'popover',\n        options.placement,\n        'popover-' + options.placement,\n        'bs-popover-' + options.placement,\n        options.popoverClass\n      ]\">\n        <div class=\"popover-arrow arrow\"></div>\n        <h3 class=\"popover-title popover-header\" [innerHTML]=\"options.title\"></h3>\n        <div class=\"popover-content popover-body\">\n          <p [innerHTML]=\"options.message\"></p>\n          <div class=\"row\">\n            <div\n              class=\"col-xs-6 col-6\"\n              [ngClass]=\"{'col-xs-offset-3 col-offset-3': options.hideCancelButton}\"\n              *ngIf=\"!options.hideConfirmButton\">\n              <button\n                [mwlFocus]=\"options.focusButton === 'confirm'\"\n                [class]=\"'btn btn-block btn-' + options.confirmButtonType\"\n                (click)=\"options.onConfirm({clickEvent: $event})\"\n                [innerHtml]=\"options.confirmText\">\n              </button>\n            </div>\n            <div\n              class=\"col-xs-6 col-6\"\n              [ngClass]=\"{'col-xs-offset-3 col-offset-3': options.hideConfirmButton}\"\n              *ngIf=\"!options.hideCancelButton\">\n              <button\n                [mwlFocus]=\"options.focusButton === 'cancel'\"\n                [class]=\"'btn btn-block btn-' + options.cancelButtonType\"\n                (click)=\"options.onCancel({clickEvent: $event})\"\n                [innerHtml]=\"options.cancelText\">\n              </button>\n            </div>\n          </div>\n        </div>\n      </div>\n    </ng-template>\n    <ng-template\n      [ngTemplateOutlet]=\"options.customTemplate || defaultTemplate\"\n      [ngTemplateOutletContext]=\"{options: options}\">\n    </ng-template>\n  "
                },] },
    ];
    /** @nocollapse */
    ConfirmationPopoverWindow.ctorParameters = function () { return [
        { type: ConfirmationPopoverWindowOptions, },
    ]; };
    return ConfirmationPopoverWindow;
}());
export { ConfirmationPopoverWindow };
//# sourceMappingURL=confirmationPopoverWindow.component.js.map