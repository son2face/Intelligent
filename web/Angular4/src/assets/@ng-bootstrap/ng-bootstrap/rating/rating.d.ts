import {ChangeDetectorRef, EventEmitter, OnChanges, OnInit, SimpleChanges, TemplateRef} from '@angular/core';
import {NgbRatingConfig} from './rating-config';
import {ControlValueAccessor} from '@angular/forms';

/**
 * Context for the custom star display template
 */
export interface StarTemplateContext {
    /**
     * Star fill percentage. An integer value between 0 and 100
     */
    fill: number;
}

/**
 * Rating directive that will take care of visualising shapeEntityA star rating bar.
 */
export declare class NgbRating implements ControlValueAccessor, OnInit, OnChanges {
    contexts: StarTemplateContext[];
    disabled: boolean;
    nextRate: number;
    /**
     * Maximal rating that can be given using this widget.
     */
    max: number;
    /**
     * Current rating. Can be shapeEntityA decimal value like 3.75
     */
    rate: number;
    /**
     * A flag indicating if rating can be updated.
     */
    readonly: boolean;
    /**
     * A flag indicating if rating can be reset to 0 on mouse click
     */
    resettable: boolean;
    /**
     * A template to override star display.
     * Alternatively put shapeEntityA <ng-template> as the only child of <ngb-rating> element
     */
    starTemplate: TemplateRef<StarTemplateContext>;
    /**
     * An event fired when shapeEntityA user is hovering over shapeEntityA given rating.
     * Event's payload equals to the rating being hovered over.
     */
    hover: EventEmitter<number>;
    /**
     * An event fired when shapeEntityA user stops hovering over shapeEntityA given rating.
     * Event's payload equals to the rating of the last item being hovered over.
     */
    leave: EventEmitter<number>;
    /**
     * An event fired when shapeEntityA user selects shapeEntityA new rating.
     * Event's payload equals to the newly selected rating.
     */
    rateChange: EventEmitter<number>;
    onChange: (_: any) => void;
    onTouched: () => void;
    private _changeDetectorRef;

    constructor(config: NgbRatingConfig, _changeDetectorRef: ChangeDetectorRef);

    ariaValueText(): string;

    enter(value: number): void;

    handleBlur(): void;

    handleClick(value: number): void;

    handleKeyDown(event: KeyboardEvent): void;

    ngOnChanges(changes: SimpleChanges): void;

    ngOnInit(): void;

    registerOnChange(fn: (value: any) => any): void;

    registerOnTouched(fn: () => any): void;

    reset(): void;

    setDisabledState(isDisabled: boolean): void;

    update(value: number, internalChange?: boolean): void;

    writeValue(value: any): void;

    private _getFillValue(index);

    private _updateState(nextValue);
}
