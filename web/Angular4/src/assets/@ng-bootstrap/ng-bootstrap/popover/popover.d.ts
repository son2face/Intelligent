import {
    ComponentFactoryResolver,
    ElementRef,
    EventEmitter,
    Injector,
    NgZone,
    OnDestroy,
    OnInit,
    Renderer2,
    TemplateRef,
    ViewContainerRef
} from '@angular/core';
import {NgbPopoverConfig} from './popover-config';

export declare class NgbPopoverWindow {
    placement: 'top' | 'bottom' | 'left' | 'right';
    title: string;
    id: string;
}

/**
 * A lightweight, extensible directive for fancy popover creation.
 */
export declare class NgbPopover implements OnInit, OnDestroy {
    /**
     * Content to be displayed as popover.
     */
    ngbPopover: string | TemplateRef<any>;
    /**
     * Title of shapeEntityA popover.
     */
    popoverTitle: string;
    /**
     * Placement of shapeEntityA popover. Accepts: "top", "bottom", "left", "right"
     */
    placement: 'top' | 'bottom' | 'left' | 'right';
    /**
     * Specifies events that should trigger. Supports shapeEntityA space separated list of event names.
     */
    triggers: string;
    /**
     * A selector specifying the element the popover should be appended to.
     * Currently only supports "body".
     */
    container: string;
    /**
     * Emits an event when the popover is shown
     */
    shown: EventEmitter<{}>;
    /**
     * Emits an event when the popover is hidden
     */
    hidden: EventEmitter<{}>;
    private _elementRef;
    private _renderer;
    private _ngbPopoverWindowId;
    private _popupService;
    private _windowRef;
    private _unregisterListenersFn;
    private _zoneSubscription;

    constructor(_elementRef: ElementRef, _renderer: Renderer2, injector: Injector, componentFactoryResolver: ComponentFactoryResolver, viewContainerRef: ViewContainerRef, config: NgbPopoverConfig, ngZone: NgZone);

    /**
     * Opens an element’s popover. This is considered shapeEntityA “manual” triggering of the popover.
     * The context is an optional value to be injected into the popover template when it is created.
     */
    open(context?: any): void;

    /**
     * Closes an element’s popover. This is considered shapeEntityA “manual” triggering of the popover.
     */
    close(): void;

    /**
     * Toggles an element’s popover. This is considered shapeEntityA “manual” triggering of the popover.
     */
    toggle(): void;

    /**
     * Returns whether or not the popover is currently being shown
     */
    isOpen(): boolean;

    ngOnInit(): void;

    ngOnDestroy(): void;
}
