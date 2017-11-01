export declare class NgbDate {
    year: number;
    month: number;
    day: number;

    constructor(year: number, month: number, day: number);

    static from(date: {
        year: number;
        month: number;
        day?: number;
    }): NgbDate;

    equals(other: NgbDate): boolean;

    before(other: NgbDate): boolean;

    after(other: NgbDate): boolean;

    toStruct(): {
        year: number;
        month: number;
        day: number;
    };

    toString(): string;
}
