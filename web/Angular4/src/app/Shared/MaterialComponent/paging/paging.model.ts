export class PagingModel {
    NumberButton: number;
    Active: number;
    TotalPage: number;
    Take: number;
    base: number;
    buttons: number[];

    constructor(NumberButton: number, Take: number, callback?: Function) {
        if (NumberButton % 2 == 0 || NumberButton < 3) throw new Error("the NumberButton must be odd and greater than 2");
        this.buttons = Array(NumberButton - 2).fill(1).map((x, i) => {
            return i + 1;
        });
        this.Take = Take;
        this.TotalPage = 0;
        this.NumberButton = NumberButton;
        this.Active = 0;
        this.base = 1;
    }

    ceil(): number {
        return Math.floor(this.NumberButton / 2 + 1);
    }

    setActive(Active: number) {
        if (Active <= this.TotalPage && Active >= 0) {
            this.Active = Active;
            let fi = Math.floor(this.NumberButton / 2 + 1);
            if (this.Active > this.TotalPage - fi) {
                this.base = this.TotalPage - fi - 2;
            } else if (this.Active > fi - 1) {
                this.base = this.Active - fi + 2;
            } else {
                this.base = 1;
            }
            this.buttons = Array(this.NumberButton - 2).fill(1).map((x, i) => {
                return this.base + i;
            });
        } else {
            throw new Error("Invalid NumberButton");
        }
    }
}