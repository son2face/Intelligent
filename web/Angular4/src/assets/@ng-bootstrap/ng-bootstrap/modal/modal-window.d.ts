import {AfterViewInit, ElementRef, EventEmitter, OnDestroy, OnInit, Renderer2} from '@angular/core';

export declare class NgbModalWindow implements OnInit, AfterViewInit, OnDestroy {
    backdrop: boolean | string;
    keyboard: boolean;
    size: string;
    windowClass: string;
    dismissEvent: EventEmitter<{}>;
    private _elRef;
    private _renderer;
    private _elWithFocus;

    constructor(_elRef: ElementRef, _renderer: Renderer2);

    backdropClick($event: any): void;

    escKey($event: any): void;

    dismiss(reason: any): void;

    ngOnInit(): void;

    ngAfterViewInit(): void;

    ngOnDestroy(): void;
}
