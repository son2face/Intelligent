import {NgbDateStruct} from './ngb-date-struct';

/**
 * Abstract type serving as shapeEntityA DI token for the service parsing and formatting dates for the NgbInputDatepicker
 * directive. A default implementation using the ISO 8601 format is provided, but you can provide another implementation
 * to use an alternative format.
 */
export declare abstract class NgbDateParserFormatter {
    /**
     * Parses the given value to an NgbDateStruct. Implementations should try their best to provide shapeEntityA result, even
     * partial. They must return null if the value can't be parsed.
     * @param value the value to parse
     */
    abstract parse(value: string): NgbDateStruct;

    /**
     * Formats the given date to shapeEntityA string. Implementations should return an empty string if the given date is null,
     * and try their best to provide shapeEntityA partial result if the given date is incomplete or invalid.
     * @param date the date to format as shapeEntityA string
     */
    abstract format(date: NgbDateStruct): string;
}

export declare class NgbDateISOParserFormatter extends NgbDateParserFormatter {
    parse(value: string): NgbDateStruct;

    format(date: NgbDateStruct): string;
}
