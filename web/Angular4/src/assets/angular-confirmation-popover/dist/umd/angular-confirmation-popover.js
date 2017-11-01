(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory(require("@angular/core"), require("@angular/common"), require("@angular/platform-browser"));
	else if(typeof define === 'function' && define.amd)
		define(["@angular/core", "@angular/common", "@angular/platform-browser"], factory);
	else if(typeof exports === 'object')
		exports["angularConfirmationPopover"] = factory(require("@angular/core"), require("@angular/common"), require("@angular/platform-browser"));
	else
		root["angularConfirmationPopover"] = factory(root["ng"]["core"], root["ng"]["common"], root["ng"]["platformBrowser"]);
})(this, function(__WEBPACK_EXTERNAL_MODULE_0__, __WEBPACK_EXTERNAL_MODULE_6__, __WEBPACK_EXTERNAL_MODULE_8__) {
return /******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 4);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE_0__;

/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

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
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(0);
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
exports.ConfirmationPopoverOptions = ConfirmationPopoverOptions;
/**
 * @private
 */
var ConfirmationPopoverWindowOptions = (function (_super) {
    __extends(ConfirmationPopoverWindowOptions, _super);
    function ConfirmationPopoverWindowOptions() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    ConfirmationPopoverWindowOptions = __decorate([
        core_1.Injectable()
    ], ConfirmationPopoverWindowOptions);
    return ConfirmationPopoverWindowOptions;
}(ConfirmationPopoverOptions));
exports.ConfirmationPopoverWindowOptions = ConfirmationPopoverWindowOptions;


/***/ }),
/* 2 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "Positioning", function() { return Positioning; });
/* harmony export (immutable) */ __webpack_exports__["positionElements"] = positionElements;
// previous version:
// https://github.com/angular-ui/bootstrap/blob/07c31d0731f7cb068a1932b8e01d2312b796b4ec/src/position/position.js
var Positioning = (function () {
    function Positioning() {
    }
    Positioning.prototype.getAllStyles = function (element) { return window.getComputedStyle(element); };
    Positioning.prototype.getStyle = function (element, prop) { return this.getAllStyles(element)[prop]; };
    Positioning.prototype.isStaticPositioned = function (element) {
        return (this.getStyle(element, 'position') || 'static') === 'static';
    };
    Positioning.prototype.offsetParent = function (element) {
        var offsetParentEl = element.offsetParent || document.documentElement;
        while (offsetParentEl && offsetParentEl !== document.documentElement && this.isStaticPositioned(offsetParentEl)) {
            offsetParentEl = offsetParentEl.offsetParent;
        }
        return offsetParentEl || document.documentElement;
    };
    Positioning.prototype.position = function (element, round) {
        if (round === void 0) { round = true; }
        var elPosition;
        var parentOffset = { width: 0, height: 0, top: 0, bottom: 0, left: 0, right: 0 };
        if (this.getStyle(element, 'position') === 'fixed') {
            elPosition = element.getBoundingClientRect();
        }
        else {
            var offsetParentEl = this.offsetParent(element);
            elPosition = this.offset(element, false);
            if (offsetParentEl !== document.documentElement) {
                parentOffset = this.offset(offsetParentEl, false);
            }
            parentOffset.top += offsetParentEl.clientTop;
            parentOffset.left += offsetParentEl.clientLeft;
        }
        elPosition.top -= parentOffset.top;
        elPosition.bottom -= parentOffset.top;
        elPosition.left -= parentOffset.left;
        elPosition.right -= parentOffset.left;
        if (round) {
            elPosition.top = Math.round(elPosition.top);
            elPosition.bottom = Math.round(elPosition.bottom);
            elPosition.left = Math.round(elPosition.left);
            elPosition.right = Math.round(elPosition.right);
        }
        return elPosition;
    };
    Positioning.prototype.offset = function (element, round) {
        if (round === void 0) { round = true; }
        var elBcr = element.getBoundingClientRect();
        var viewportOffset = {
            top: window.pageYOffset - document.documentElement.clientTop,
            left: window.pageXOffset - document.documentElement.clientLeft
        };
        var elOffset = {
            height: elBcr.height || element.offsetHeight,
            width: elBcr.width || element.offsetWidth,
            top: elBcr.top + viewportOffset.top,
            bottom: elBcr.bottom + viewportOffset.top,
            left: elBcr.left + viewportOffset.left,
            right: elBcr.right + viewportOffset.left
        };
        if (round) {
            elOffset.height = Math.round(elOffset.height);
            elOffset.width = Math.round(elOffset.width);
            elOffset.top = Math.round(elOffset.top);
            elOffset.bottom = Math.round(elOffset.bottom);
            elOffset.left = Math.round(elOffset.left);
            elOffset.right = Math.round(elOffset.right);
        }
        return elOffset;
    };
    Positioning.prototype.positionElements = function (hostElement, targetElement, placement, appendToBody) {
        var hostElPosition = appendToBody ? this.offset(hostElement, false) : this.position(hostElement, false);
        var targetElStyles = this.getAllStyles(targetElement);
        var targetElBCR = targetElement.getBoundingClientRect();
        var placementPrimary = placement.split('-')[0] || 'top';
        var placementSecondary = placement.split('-')[1] || 'center';
        var targetElPosition = {
            'height': targetElBCR.height || targetElement.offsetHeight,
            'width': targetElBCR.width || targetElement.offsetWidth,
            'top': 0,
            'bottom': targetElBCR.height || targetElement.offsetHeight,
            'left': 0,
            'right': targetElBCR.width || targetElement.offsetWidth
        };
        switch (placementPrimary) {
            case 'top':
                targetElPosition.top =
                    hostElPosition.top - (targetElement.offsetHeight + parseFloat(targetElStyles.marginBottom));
                break;
            case 'bottom':
                targetElPosition.top = hostElPosition.top + hostElPosition.height;
                break;
            case 'left':
                targetElPosition.left =
                    hostElPosition.left - (targetElement.offsetWidth + parseFloat(targetElStyles.marginRight));
                break;
            case 'right':
                targetElPosition.left = hostElPosition.left + hostElPosition.width;
                break;
        }
        switch (placementSecondary) {
            case 'top':
                targetElPosition.top = hostElPosition.top;
                break;
            case 'bottom':
                targetElPosition.top = hostElPosition.top + hostElPosition.height - targetElement.offsetHeight;
                break;
            case 'left':
                targetElPosition.left = hostElPosition.left;
                break;
            case 'right':
                targetElPosition.left = hostElPosition.left + hostElPosition.width - targetElement.offsetWidth;
                break;
            case 'center':
                if (placementPrimary === 'top' || placementPrimary === 'bottom') {
                    targetElPosition.left = hostElPosition.left + hostElPosition.width / 2 - targetElement.offsetWidth / 2;
                }
                else {
                    targetElPosition.top = hostElPosition.top + hostElPosition.height / 2 - targetElement.offsetHeight / 2;
                }
                break;
        }
        targetElPosition.top = Math.round(targetElPosition.top);
        targetElPosition.bottom = Math.round(targetElPosition.bottom);
        targetElPosition.left = Math.round(targetElPosition.left);
        targetElPosition.right = Math.round(targetElPosition.right);
        return targetElPosition;
    };
    return Positioning;
}());

var positionService = new Positioning();
function positionElements(hostElement, targetElement, placement, appendToBody) {
    var pos = positionService.positionElements(hostElement, targetElement, placement, appendToBody);
    targetElement.style.top = pos.top + "px";
    targetElement.style.left = pos.left + "px";
}
//# sourceMappingURL=positioning.js.map

/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(0);
var confirmationPopoverOptions_provider_1 = __webpack_require__(1);
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
    ConfirmationPopoverWindow = __decorate([
        core_1.Component({
            styles: ["\n    .popover {\n      display: block;\n    }\n    .bs-popover-top .arrow, .bs-popover-bottom .arrow {\n      left: 50%;\n    }\n    .bs-popover-left .arrow, .bs-popover-right .arrow {\n      top: 50%;\n    }\n    .btn {\n      transition: none;\n    }\n  "],
            template: "\n    <ng-template #defaultTemplate let-options=\"options\">\n      <div [ngClass]=\"[\n        'popover',\n        options.placement,\n        'popover-' + options.placement,\n        'bs-popover-' + options.placement,\n        options.popoverClass\n      ]\">\n        <div class=\"popover-arrow arrow\"></div>\n        <h3 class=\"popover-title popover-header\" [innerHTML]=\"options.title\"></h3>\n        <div class=\"popover-content popover-body\">\n          <p [innerHTML]=\"options.message\"></p>\n          <div class=\"row\">\n            <div\n              class=\"col-xs-6 col-6\"\n              [ngClass]=\"{'col-xs-offset-3 col-offset-3': options.hideCancelButton}\"\n              *ngIf=\"!options.hideConfirmButton\">\n              <button\n                [mwlFocus]=\"options.focusButton === 'confirm'\"\n                [class]=\"'btn btn-block btn-' + options.confirmButtonType\"\n                (click)=\"options.onConfirm({clickEvent: $event})\"\n                [innerHtml]=\"options.confirmText\">\n              </button>\n            </div>\n            <div\n              class=\"col-xs-6 col-6\"\n              [ngClass]=\"{'col-xs-offset-3 col-offset-3': options.hideConfirmButton}\"\n              *ngIf=\"!options.hideCancelButton\">\n              <button\n                [mwlFocus]=\"options.focusButton === 'cancel'\"\n                [class]=\"'btn btn-block btn-' + options.cancelButtonType\"\n                (click)=\"options.onCancel({clickEvent: $event})\"\n                [innerHtml]=\"options.cancelText\">\n              </button>\n            </div>\n          </div>\n        </div>\n      </div>\n    </ng-template>\n    <ng-template\n      [ngTemplateOutlet]=\"options.customTemplate || defaultTemplate\"\n      [ngTemplateOutletContext]=\"{options: options}\">\n    </ng-template>\n  "
        }),
        __metadata("design:paramtypes", [confirmationPopoverOptions_provider_1.ConfirmationPopoverWindowOptions])
    ], ConfirmationPopoverWindow);
    return ConfirmationPopoverWindow;
}());
exports.ConfirmationPopoverWindow = ConfirmationPopoverWindow;


/***/ }),
/* 4 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

Object.defineProperty(exports, "__esModule", { value: true });
var confirmationPopover_module_1 = __webpack_require__(5);
exports.ConfirmationPopoverModule = confirmationPopover_module_1.ConfirmationPopoverModule;


/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(0);
var common_1 = __webpack_require__(6);
var positioning_1 = __webpack_require__(2);
var confirmationPopover_directive_1 = __webpack_require__(7);
var confirmationPopoverWindow_component_1 = __webpack_require__(3);
var focus_directive_1 = __webpack_require__(9);
var confirmationPopoverOptions_provider_1 = __webpack_require__(1);
exports.USER_OPTIONS = new core_1.InjectionToken('confirmation popover user options');
function optionsFactory(userOptions) {
    var options = new confirmationPopoverOptions_provider_1.ConfirmationPopoverOptions();
    Object.assign(options, userOptions);
    return options;
}
exports.optionsFactory = optionsFactory;
var ConfirmationPopoverModule = (function () {
    function ConfirmationPopoverModule() {
    }
    ConfirmationPopoverModule_1 = ConfirmationPopoverModule;
    ConfirmationPopoverModule.forRoot = function (options) {
        if (options === void 0) { options = {}; }
        return {
            ngModule: ConfirmationPopoverModule_1,
            providers: [{
                    provide: exports.USER_OPTIONS,
                    useValue: options
                }, {
                    provide: confirmationPopoverOptions_provider_1.ConfirmationPopoverOptions,
                    useFactory: optionsFactory,
                    deps: [exports.USER_OPTIONS]
                }, positioning_1.Positioning]
        };
    };
    ConfirmationPopoverModule = ConfirmationPopoverModule_1 = __decorate([
        core_1.NgModule({
            declarations: [confirmationPopover_directive_1.ConfirmationPopover, confirmationPopoverWindow_component_1.ConfirmationPopoverWindow, focus_directive_1.Focus],
            imports: [common_1.CommonModule],
            exports: [confirmationPopover_directive_1.ConfirmationPopover, focus_directive_1.Focus],
            entryComponents: [confirmationPopoverWindow_component_1.ConfirmationPopoverWindow]
        })
    ], ConfirmationPopoverModule);
    return ConfirmationPopoverModule;
    var ConfirmationPopoverModule_1;
}());
exports.ConfirmationPopoverModule = ConfirmationPopoverModule;


/***/ }),
/* 6 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE_6__;

/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(0);
var platform_browser_1 = __webpack_require__(8);
var confirmationPopoverWindow_component_1 = __webpack_require__(3);
var confirmationPopoverOptions_provider_1 = __webpack_require__(1);
var positioning_1 = __webpack_require__(2);
/**
 * All properties can be set on the directive as attributes like so (use `ConfirmationPopoverModule.forRoot()` to configure them globally):
 * ```html
 * &lt;button
 *  class="btn btn-default"
 *  mwlConfirmationPopover
 *  [title]="title"
 *  [message]="message"
 *  placement="left"
 *  (confirm)="confirmClicked = true"
 *  (cancel)="cancelClicked = true"
 *  [(isOpen)]="isOpen"&gt;
 *   Show confirm popover!
 * &lt;/button&gt;
 * ```
 */
var ConfirmationPopover = (function () {
    /**
     * @private
     */
    function ConfirmationPopover(viewContainerRef, elm, defaultOptions, cfr, position, renderer, document //tslint:disable-line
    ) {
        this.viewContainerRef = viewContainerRef;
        this.elm = elm;
        this.defaultOptions = defaultOptions;
        this.cfr = cfr;
        this.position = position;
        this.renderer = renderer;
        this.document = document; //tslint:disable-line
        /**
         * Whether to disable showing the popover. Default `false`.
         */
        this.isDisabled = false;
        /**
         * Will open or show the popover when changed.
         * Can be sugared with `isOpenChange` to emulate 2-way binding like so `[(isOpen)]="isOpen"`
         */
        this.isOpen = false;
        /**
         * Will emit when the popover is opened or closed
         */
        this.isOpenChange = new core_1.EventEmitter(true);
        /**
         * An expression that is called when the confirm button is clicked.
         */
        this.confirm = new core_1.EventEmitter();
        /**
         * An expression that is called when the cancel button is clicked.
         */
        this.cancel = new core_1.EventEmitter();
        /**
         * @private
         */
        this.popover = null;
        /**
         * @private
         */
        this.eventListeners = [];
    }
    /**
     * @private
     */
    ConfirmationPopover.prototype.ngOnInit = function () {
        this.isOpenChange.emit(false);
    };
    /**
     * @private
     */
    ConfirmationPopover.prototype.ngOnChanges = function (changes) {
        if (changes.isOpen) {
            if (changes.isOpen.currentValue === true) {
                this.showPopover();
            }
            else {
                this.hidePopover();
            }
        }
    };
    /**
     * @private
     */
    ConfirmationPopover.prototype.ngOnDestroy = function () {
        this.hidePopover();
    };
    /**
     * @private
     */
    ConfirmationPopover.prototype.onConfirm = function (event) {
        this.confirm.emit(event);
        this.hidePopover();
    };
    /**
     * @private
     */
    ConfirmationPopover.prototype.onCancel = function (event) {
        this.cancel.emit(event);
        this.hidePopover();
    };
    /**
     * @private
     */
    ConfirmationPopover.prototype.togglePopover = function () {
        if (!this.popover) {
            this.showPopover();
        }
        else {
            this.hidePopover();
        }
    };
    ConfirmationPopover.prototype.onDocumentClick = function (event) {
        if (this.popover && !this.elm.nativeElement.contains(event.target) && !this.popover.location.nativeElement.contains(event.target)) {
            this.hidePopover();
        }
    };
    ConfirmationPopover.prototype.showPopover = function () {
        var _this = this;
        if (!this.popover && !this.isDisabled) {
            this.eventListeners = [
                this.renderer.listen('document', 'click', function (event) { return _this.onDocumentClick(event); }),
                this.renderer.listen('document', 'touchend', function (event) { return _this.onDocumentClick(event); }),
                this.renderer.listen('window', 'resize', function () { return _this.positionPopover(); })
            ];
            var options_1 = new confirmationPopoverOptions_provider_1.ConfirmationPopoverWindowOptions();
            Object.assign(options_1, this.defaultOptions, {
                title: this.title,
                message: this.message,
                onConfirm: function (event) {
                    _this.onConfirm(event);
                },
                onCancel: function (event) {
                    _this.onCancel(event);
                },
                onAfterViewInit: function () {
                    _this.positionPopover();
                }
            });
            var optionalParams = [
                'confirmText',
                'cancelText',
                'placement',
                'confirmButtonType',
                'cancelButtonType',
                'focusButton',
                'hideConfirmButton',
                'hideCancelButton',
                'popoverClass',
                'appendToBody',
                'customTemplate'
            ];
            optionalParams.forEach(function (param) {
                if (typeof _this[param] !== 'undefined') {
                    options_1[param] = _this[param];
                }
            });
            var componentFactory = this.cfr.resolveComponentFactory(confirmationPopoverWindow_component_1.ConfirmationPopoverWindow);
            var binding = core_1.ReflectiveInjector.resolve([{
                    provide: confirmationPopoverOptions_provider_1.ConfirmationPopoverWindowOptions,
                    useValue: options_1
                }]);
            var contextInjector = this.viewContainerRef.parentInjector;
            var childInjector = core_1.ReflectiveInjector.fromResolvedProviders(binding, contextInjector);
            this.popover = this.viewContainerRef.createComponent(componentFactory, this.viewContainerRef.length, childInjector);
            if (options_1.appendToBody) {
                this.document.body.appendChild(this.popover.location.nativeElement);
            }
            this.isOpenChange.emit(true);
        }
    };
    ConfirmationPopover.prototype.positionPopover = function () {
        if (this.popover) {
            var popoverElement = this.popover.location.nativeElement.children[0];
            var popoverPosition = this.position.positionElements(this.elm.nativeElement, popoverElement, this.placement || this.defaultOptions.placement, this.appendToBody || this.defaultOptions.appendToBody);
            this.renderer.setStyle(popoverElement, 'top', popoverPosition.top + "px");
            this.renderer.setStyle(popoverElement, 'left', popoverPosition.left + "px");
        }
    };
    ConfirmationPopover.prototype.hidePopover = function () {
        if (this.popover) {
            this.popover.destroy();
            this.popover = null;
            this.isOpenChange.emit(false);
            this.eventListeners.forEach(function (fn) { return fn(); });
            this.eventListeners = [];
        }
    };
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ConfirmationPopover.prototype, "title", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ConfirmationPopover.prototype, "message", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ConfirmationPopover.prototype, "confirmText", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ConfirmationPopover.prototype, "cancelText", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ConfirmationPopover.prototype, "placement", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ConfirmationPopover.prototype, "confirmButtonType", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ConfirmationPopover.prototype, "cancelButtonType", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ConfirmationPopover.prototype, "focusButton", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], ConfirmationPopover.prototype, "hideConfirmButton", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], ConfirmationPopover.prototype, "hideCancelButton", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], ConfirmationPopover.prototype, "isDisabled", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], ConfirmationPopover.prototype, "isOpen", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", core_1.TemplateRef)
    ], ConfirmationPopover.prototype, "customTemplate", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], ConfirmationPopover.prototype, "isOpenChange", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], ConfirmationPopover.prototype, "confirm", void 0);
    __decorate([
        core_1.Output(),
        __metadata("design:type", core_1.EventEmitter)
    ], ConfirmationPopover.prototype, "cancel", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", String)
    ], ConfirmationPopover.prototype, "popoverClass", void 0);
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], ConfirmationPopover.prototype, "appendToBody", void 0);
    __decorate([
        core_1.HostListener('click'),
        __metadata("design:type", Function),
        __metadata("design:paramtypes", []),
        __metadata("design:returntype", void 0)
    ], ConfirmationPopover.prototype, "togglePopover", null);
    ConfirmationPopover = __decorate([
        core_1.Directive({
            selector: '[mwlConfirmationPopover]'
        }),
        __param(6, core_1.Inject(platform_browser_1.DOCUMENT)),
        __metadata("design:paramtypes", [core_1.ViewContainerRef,
            core_1.ElementRef,
            confirmationPopoverOptions_provider_1.ConfirmationPopoverOptions,
            core_1.ComponentFactoryResolver,
            positioning_1.Positioning,
            core_1.Renderer2, Object])
    ], ConfirmationPopover);
    return ConfirmationPopover;
}());
exports.ConfirmationPopover = ConfirmationPopover;


/***/ }),
/* 8 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE_8__;

/***/ }),
/* 9 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = __webpack_require__(0);
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
    __decorate([
        core_1.Input(),
        __metadata("design:type", Boolean)
    ], Focus.prototype, "mwlFocus", void 0);
    Focus = __decorate([
        core_1.Directive({
            selector: '[mwlFocus]'
        }),
        __metadata("design:paramtypes", [core_1.ElementRef])
    ], Focus);
    return Focus;
}());
exports.Focus = Focus;


/***/ })
/******/ ]);
});
//# sourceMappingURL=angular-confirmation-popover.js.map