import { Directive, ElementRef, Input } from '@angular/core';
/**
 * A helper directive to focus buttons. You will only need this if using a custom template
 */
var Focus = (function () {
    function Focus(elm) {
        this.elm = elm;
    }
    Focus.prototype.ngOnChanges = function (changes) {
        if (changes.mwlFocus && this.mwlFocus === true) {
            this.elm.nativeElement.focus();
        }
    };
    Focus.decorators = [
        { type: Directive, args: [{
                    selector: '[mwlFocus]'
                },] },
    ];
    /** @nocollapse */
    Focus.ctorParameters = function () { return [
        { type: ElementRef, },
    ]; };
    Focus.propDecorators = {
        'mwlFocus': [{ type: Input },],
    };
    return Focus;
}());
export { Focus };
//# sourceMappingURL=focus.directive.js.map