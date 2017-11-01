export class FilterComponentEntity {
    Id: string;
    Name: string;
    IsEdit: boolean;

    constructor(ADGroup: any = null) {
        if (ADGroup == null) {
            this.Id = null;
            this.Name = null;
        } else {
            this.Id = ADGroup.Id;
            this.Name = ADGroup.Name;
        }
    }
}