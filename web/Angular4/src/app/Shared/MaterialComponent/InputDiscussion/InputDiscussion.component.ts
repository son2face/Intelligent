import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
    selector: 'InputDiscussion',
    templateUrl: './InputDiscussion.component.html',
    styleUrls: ['./InputDiscussion.component.css']

})
export class InputDiscussionComponent implements OnInit {
    static DiscussionComponentList: Array<InputDiscussionComponent> = [];
    static IsBlock: boolean = false;
    public IsFirstClick: boolean = true;
    @Input() isTurnOffAnother: boolean = true;
    @Input() DataList: Array<any> = [{}];
    @Output() onEnter: EventEmitter<any> = new EventEmitter();
    @Output() onSearchWhoAt: EventEmitter<any> = new EventEmitter();
    @Output() ToolUpdate: EventEmitter<any> = new EventEmitter();
    @Input() ChangeWhenUpDown: boolean = false;
    @Input() AutoReplaceWhenClick = false;
    @Input() IsLoopOption: boolean = false;
    @Input() IsValidateDataList: boolean = true;
    @Input() PropertyActive: string = "IsActive";
    @Input() PropertySelected: string = "IsSelected";
    @Input() IsError: boolean = false;
    @Output() onFirstClick: EventEmitter<any> = new EventEmitter();
    @Input() ClassContent: string = "";
    @Input() AtWhoClass: string = "AtWho";
    @Input() SearchWhoClass: string = "SearchWho";
    @Input() DistantPopUp: number = 2;
    @Input() Placement: string = "down";
    @Input() InputClass: string = "InputComment";
    @Input() TextBlockClass: string = "";
    @Input() DisplayProperty: string = "Name";
    @Input() Id: string = this.MakeRandomId();
    @Input() BindingObject: any = {Content: ''};
    @Input() PropertyToBindWithContent: string = "Content";
    @Input() DisableWhenEnter: boolean = false;
    TopPopUp: number = 0;
    LeftPopUp: number = 0;
    CurrentNumber: number = -1;
    IsShow: boolean = false;

    constructor() {
        InputDiscussionComponent.DiscussionComponentList.push(this);
    }

    ngOnInit() {
    }

    private MakeRandomId(): string {
        let text = "";
        let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (let i = 0; i < 5; i++)
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        return text;
    }

    IsOut = true;

    OnKeyPress(event): boolean {
        // console.log(event);
        let Result = true;
        if (window.getSelection) {
            let sel = window.getSelection();
            if (sel.getRangeAt && sel.rangeCount) {
                let range = sel.getRangeAt(0);
                let Carpet = range.startOffset;  // Lưu vị trí của con trỏ
                let SelectedElement: any = sel; // Lưu element hiện tại
                // Xong đoạn tìm vị trí
                let ElementType = SelectedElement.anchorNode.parentNode.getAttribute("DiscussionType");
                if (ElementType != null && ElementType == "Who") {
                    return false;
                } else if (event.keyCode == 13 && this.IsShow == true) {
                    this.TurnOff(event);
                    event.preventDefault();
                    this.IsOut = false;
                    return false;
                } else if (event.keyCode == 13 && this.IsShow == false && this.DisableWhenEnter == true) {
                    this.TurnOff(event);
                    this.IsOut = true;
                    this.BindingObject[this.PropertyToBindWithContent] = document.getElementById(this.Id).innerHTML;
                    document.getElementById(this.Id).innerHTML = "";
                    this.onEnter.emit(this.BindingObject);
                    event.preventDefault();
                    return false;
                }
                else if (event.key == '@') {
                    SelectedElement.anchorNode.childNodes.forEach(X => {
                        if (X.nodeName == "BR" || X.nodeName == "FONT")
                            SelectedElement.anchorNode.removeChild(X);
                    });
                    Result = !this.ProcessWithAtCharacter(Carpet, SelectedElement, range);
                    SelectedElement.anchorNode.childNodes.forEach(X => {
                        if (X.nodeName == "BR" || X.nodeName == "FONT")
                            SelectedElement.anchorNode.removeChild(X);
                    });
                } else if (event.key == ' ' || event.key == String.fromCharCode(160)) {
                    Result = Result && !this.ProcessWithSpaceCharacters(Carpet, SelectedElement, range);
                } else if (SelectedElement.anchorNode.nodeName == "#text" && SelectedElement.anchorNode.parentNode.nodeName == "FONT") {
                    let d = this.CreateSimpleTextElemnt();
                    d.innerHTML = SelectedElement.anchorNode.textContent;
                    SelectedElement.anchorNode.parentNode.parentNode.replaceChild(d, SelectedElement.anchorNode.parentNode);
                    let range1 = document.createRange();
                    range1.setStart(d, Carpet);
                    range1.collapse(true);
                    SelectedElement.removeAllRanges();
                    SelectedElement.addRange(range1);
                }
                else if (SelectedElement.anchorNode.nodeName == "DIV") {
                    let d = this.CreateSimpleTextElemnt();
                    d.innerHTML = event.key;
                    SelectedElement.anchorNode.childNodes.forEach(X => {
                        if (X.nodeName == "BR" || X.nodeName == "FONT")
                            SelectedElement.anchorNode.removeChild(X);
                    });
                    let frag = document.createDocumentFragment();
                    frag.appendChild(d);
                    range.insertNode(frag);
                    range = range.cloneRange();
                    range.collapse(true);
                    range.setStart(d, 1);
                    SelectedElement.removeAllRanges();
                    SelectedElement.addRange(range);
                    Result = false;
                }
                this.TurnOnPopUpWhenCaretIsOn();
            }
        }
        return Result;
    }

    OnKeyUp(event): boolean {
        if (this.IsShow == true) {
            switch (event.keyCode) {
                case 38:
                    this.Up();
                    event.preventDefault();
                    return false;
                case 40:
                    this.Down();
                    event.preventDefault();
                    return false;
                case 27:
                    this.Esc();
                    event.preventDefault();
                    return false;
                case 13:
                    this.TurnOff(event);
                    event.preventDefault();
                    return false;
                default :
                    break;
            }
        }
        else if (event.keyCode == 13 && this.DisableWhenEnter == true && this.IsOut == true) {
            document.getElementById(this.Id).innerHTML = "";
            return;
        }
        this.TurnOnPopUpWhenCaretIsOn(event);
        return true;
    }

    OnClick(event) {
        this.TurnOnPopUpWhenCaretIsOn();
        // console.log(lastNode.nextSibling);
        // document.getElementById("None" + Id).addEventListener('click', x => {
        //     this.OnClickAtPosition(Id, x);
        //     this.ComputePositionPopUp(x.target);
        // });
        // document.getElementById("None" + Id).addEventListener('keydown', x => {
        //     this.OnKeyDown(Id, x);
        // });
        // document.getElementById("None" + Id).addEventListener('hover', x => {
        //     this.OnHover(Id, x);
        // });
        // document.getElementById("None" + Id).click();
    }

    // IsPreviousCtrlA: boolean = false;

    TurnOnPopUpWhenCaretIsOn(event?) {
        if (window.getSelection) {
            // IE9 and non-IE
            let sel = window.getSelection();
            let Elem: any = sel.anchorNode.parentNode;
            console.log(event);
            if (sel.getRangeAt && sel.rangeCount) {
                if (Elem.id.indexOf("None") == 0) {
                    this.ComputePositionPopUp(Elem);
                    setTimeout(e => {
                        let Result: any = {};
                        Result.Element = Elem;
                        Result.SearchData = Elem.textContent.substr(1, Elem.textContent.length);
                        Result.ReplaceElement = (id, name) => {
                            let el = this.CreateWhoElement(id);
                            el.innerHTML = name;
                            Elem.parentNode.replaceChild(el, Elem);
                        };
                        this.ElemToReplace = Elem;
                        this.onSearchWhoAt.emit(Result);
                    }, 50);
                    this.IsShow = true;
                } else {
                    this.IsShow = false;
                    let Attribute = Elem.getAttribute("DiscussionType");
                    if (Attribute != null && Attribute == "Who") {
                        if (event != null) {
                            switch (event.keyCode) {
                                case 8:
                                    Elem.parentNode.removeChild(Elem);
                                    break;
                                case 46:
                                    Elem.parentNode.removeChild(Elem);
                                    break;
                                default :
                                    break;
                            }
                        }
                    }
                    // if (Attribute != null && Attribute == "Who") {
                    //     if (this.IsPreviousCtrlA == false) {
                    //         let range1 = document.createRange();
                    //         if (event != null) {
                    //             if (event.ctrlKey) {
                    //                 if (event.keyCode == 65 || event.keyCode == 97)
                    //                     this.IsPreviousCtrlA = true;
                    //                 return;
                    //             }
                    //             switch (event.keyCode) {
                    //                 case 37 :
                    //                     // range1.setStartAfter(Elem);
                    //                     break;
                    //                 case 17:
                    //                     return;
                    //                 default :
                    //                     range1.setStart(Elem.firstChild, Elem.firstChild.nodeValue.length);
                    //                     break;
                    //             }
                    //         } else {
                    //             range1.setStart(Elem.firstChild, Elem.firstChild.nodeValue.length);
                    //         }
                    //         range1.setStart(Elem.firstChild, Elem.firstChild.nodeValue.length);
                    //         range1.collapse(true);
                    //         sel.removeAllRanges();
                    //         sel.addRange(range1);
                    //     } else if (event == null || event.keyCode != 17) {
                    //         this.IsPreviousCtrlA = false;
                    //     }
                    // }
                }
            }
        }
    }

    ProcessWithDeleteCharacter(Carpet, SelectedElement, range): boolean {
        console.log(SelectedElement);
        if (SelectedElement.anchorNode.parentNode.id.indexOf("None") == 0) {
            let CarpetElement = SelectedElement.anchorNode.parentNode.nextSibling;
            if (SelectedElement.anchorNode.parentNode.nextSibling == null) {
                CarpetElement = this.CreateSimpleTextElemnt();
                SelectedElement.anchorNode.parentNode.parentNode.appendChild(CarpetElement);
            }
            CarpetElement.innerHTML = "&nbsp;" + CarpetElement.innerHTML;
            let range1 = document.createRange();
            range1.setStart(CarpetElement.firstChild, 1);
            range1.collapse(true);
            SelectedElement.removeAllRanges();
            SelectedElement.addRange(range1);
            event.preventDefault();
            return true;
        }
    }

    ProcessWithSpaceCharacters(Carpet, SelectedElement, range): boolean {
        // console.log(SelectedElement);
        if (SelectedElement.anchorNode.parentNode.id.indexOf("None") == 0) {
            this.IsShow = false;
            let CarpetElement = SelectedElement.anchorNode.parentNode.nextSibling;
            if (CarpetElement == null) {
                let d = this.CreateSimpleTextElemnt();
                SelectedElement.anchorNode.parentNode.parentNode.appendChild(d);
                CarpetElement = d;
            }
            CarpetElement.innerHTML = "&nbsp;" + CarpetElement.innerHTML;
            let range1 = document.createRange();
            range1.setStart(CarpetElement.firstChild, 1);
            range1.collapse(true);
            SelectedElement.removeAllRanges();
            SelectedElement.addRange(range1);
            event.preventDefault();
            return true;
        }
    }

    CreateSearchWhoTextElemnt() {
        let el = document.createElement("span");
        let Id = this.MakeRandomId();
        el.className = this.SearchWhoClass;
        el.id = "None" + Id;
        el.setAttribute("DiscussionType", "SearchWhoText");
        return el;
    }

    CreateSimpleTextElemnt() {
        let d = document.createElement("span");
        d.className = this.TextBlockClass;
        d.setAttribute("DiscussionType", "SimpleText");
        return d;
    }

    CreateWhoElement(id) {
        let d = document.createElement("span");
        d.className = this.AtWhoClass;
        d.setAttribute("DiscussionType", "Who");
        d.setAttribute("Id-Entity", id);
        return d;
    }

    ProcessWithAtCharacter(Carpet, SelectedElement, range): boolean {
        if (Carpet > 0 && SelectedElement.anchorNode.nodeValue !== null && SelectedElement.anchorNode.nodeValue[Carpet - 1] !== " " && SelectedElement.anchorNode.nodeValue[Carpet - 1] !== String.fromCharCode(160)) {
            // console.log("Trường hợp loại");
            return false;
        }
        let el = this.CreateSearchWhoTextElemnt();
        el.innerHTML = "@";
        let frag = document.createDocumentFragment();
        frag.appendChild(el);
        range.insertNode(frag);
        range = range.cloneRange();
        range.collapse(true);
        range.setStart(el, 1);
        SelectedElement.removeAllRanges();
        SelectedElement.addRange(range);
        event.preventDefault();
        SelectedElement.anchorNode.parentNode.childNodes.forEach(X => {
            if (X.nodeName == "#text") {
                let d = this.CreateSimpleTextElemnt();
                d.innerHTML = X.nodeValue;
                X.replaceWith(d);
            }
        });
        if (SelectedElement.anchorNode.parentNode.getAttribute("DiscussionType") == "SimpleText") {
            let parentNode = SelectedElement.anchorNode.parentNode;
            let list = SelectedElement.anchorNode.parentNode.childNodes;
            let Arr = [];
            for (let i = 0; i < list.length; i++) {
                Arr.push(list[i].cloneNode(true));
                if (list[i] == el) {
                    el = Arr[i]
                }
            }
            for (let i = 0; i < Arr.length; i++) {
                SelectedElement.anchorNode.parentNode.parentNode.insertBefore(Arr[i], parentNode);
            }
            SelectedElement.anchorNode.parentNode.parentNode.removeChild(SelectedElement.anchorNode.parentNode);
        }
        let range1 = document.createRange();
        range1.setStart(el, 1);
        range1.collapse(true);
        SelectedElement.removeAllRanges();
        SelectedElement.addRange(range1);
        return true;
    }

    @Input()
    OnClickAtPosition(Id: string, event) {
        this.IsShow = true;
        if (Id.indexOf("None") == 0) {
            let SelectedElement = event.target;
            let range = event.target.getRangeAt(0);
            // Preserve the selection
            range = range.cloneRange();
            range.setStartAfter(event.target);
            // range.setStartAfter(lastNode);
            range.collapse(true);
            // SelectedElement.removeAllRanges();
            SelectedElement.addRange(range);
        }
        console.log(event);
    }


    @Input()
    OnHover(Id, event) {
        console.log(event);
    }

    ComputePositionPopUp(ElementData) {
        let Position = ElementData.getBoundingClientRect();
        switch (this.Placement) {
            case "down":
                this.LeftPopUp = Position.left;
                this.TopPopUp = Position.top + Position.height + this.DistantPopUp;
                break;
            case "top":
                this.TopPopUp = Position.top - this.DistantPopUp;
                this.LeftPopUp = Position.left;
                console.log("chưa hoàn thiện !");
                break;
            case "right":
                this.TopPopUp = Position.top;
                this.LeftPopUp = Position.left + Position.width + this.DistantPopUp;
                console.log("chưa hoàn thiện !");
                break;
            case "left":
                this.TopPopUp = Position.top;
                this.LeftPopUp = Position.left - this.DistantPopUp;
                console.log("chưa hoàn thiện !");
                break;
        }
    }

    TurnOn() {
        this.IsShow = true;
        InputDiscussionComponent.IsBlock = true;
        setTimeout(() => {
            InputDiscussionComponent.IsBlock = false;
            this.TurnOffAnother();
        }, 40);
    }

    Click() {
        if (this.IsFirstClick) {
            this.IsFirstClick = false;
            this.onFirstClick.emit();
        }
        if (this.IsShow) {
            this.IsShow = false;
            return;
        }
        this.Remove();
        this.ValidateDataList();
        this.TurnOn();
    }

    Esc() {
        if (this.IsShow) {
            this.IsShow = false;
            return;
        }
        this.Remove();
        this.TurnOn();
    }

    // DbClick() {
    //     this.Remove();
    //     this.ValidateDataList();
    //     this.TurnOn();
    // }
    ElemToReplace: any = null;

    TurnOff(event?) {
        // if (this.isMultiSelect || this.IsTree) {
        //     this.TurnOn();
        //     return;
        // }
        if (this.DataList != null && this.ElemToReplace != null && this.AutoReplaceWhenClick == true) {
            console.log(this.DataList);
            let Data = this.DataList.find(X => {
                return X.IsActive || X.IsSelected;
            });
            let el = this.CreateWhoElement(Data.Id);
            el.innerHTML = Data[this.DisplayProperty];
            this.ElemToReplace.parentNode.replaceChild(el, this.ElemToReplace);
            this.ElemToReplace = null;
            let SelectedElement: any = window.getSelection();
            this.IsShow = false;
            let CarpetElement: any = el.nextSibling;
            if (CarpetElement == null) {
                let d = this.CreateSimpleTextElemnt();
                el.parentNode.appendChild(d);
                CarpetElement = d;
            }
            if (CarpetElement.innerHTML.length > 0 && (CarpetElement.innerHTML[0] == ' ' || CarpetElement.innerHTML[0] == String.fromCharCode(160) )) {
            } else CarpetElement.innerHTML = "&nbsp;" + CarpetElement.innerHTML;
            let range1 = document.createRange();
            range1.setStart(CarpetElement.firstChild, 1);
            range1.collapse(true);
            SelectedElement.removeAllRanges();
            SelectedElement.addRange(range1);
            event.preventDefault();
        }
        this.IsShow = false;
    }

    TurnOffAnother() {
        if (this.isTurnOffAnother == false || InputDiscussionComponent.IsBlock) return;
        InputDiscussionComponent.DiscussionComponentList.forEach(DC => {
            if (DC.Id != this.Id) DC.IsShow = false;
        })
    }

    Up() {
        if (this.DataList == null || this.DataList.length == 0) return;
        let length = this.DataList.length;
        for (let i = 0; i < length; i++) {
            if (this.DataList[i][this.PropertyActive] == true) {
                this.DataList[i][this.PropertyActive] = false;
                if (i == 0) {
                    if (this.IsLoopOption) this.DataList[length - 1][this.PropertyActive] = true;
                    else this.DataList[0][this.PropertyActive] = true;
                } else {
                    this.DataList[i - 1][this.PropertyActive] = true;
                }
                if (this.ChangeWhenUpDown == true) {
                    this.Enter();
                    this.TurnOn();
                }
                return;
            }
        }
        this.DataList[0][this.PropertyActive] = true;
        if (this.ChangeWhenUpDown == true) {
            this.Enter();
            this.TurnOn();
        }
    }

    Down() {
        if (this.DataList == null || this.DataList.length == 0) return;
        let length = this.DataList.length;
        for (let i = 0; i < length; i++) {
            if (this.DataList[i][this.PropertyActive] == true) {
                this.DataList[i][this.PropertyActive] = false;
                if (i == length - 1) {
                    if (this.IsLoopOption) this.DataList[0][this.PropertyActive] = true;
                    else this.DataList[length - 1][this.PropertyActive] = true;
                } else {
                    this.DataList[i + 1][this.PropertyActive] = true;
                }
                if (this.ChangeWhenUpDown == true) {
                    this.Enter();
                    this.TurnOn();
                }
                return;
            }
        }
        this.DataList[0][this.PropertyActive] = true;
        if (this.ChangeWhenUpDown == true) {
            this.Enter();
            this.TurnOn();
        }
    }

    Remove() {
        if (this.DataList == null) return;
        let length = this.DataList.length;
        for (let i = 0; i < length; i++) {
            this.DataList[i][this.PropertyActive] = false;
        }
    }

    Enter() {
        if (this.DataList == null) return;
        let length = this.DataList.length;
        for (let i = 0; i < length; i++) {
            if (this.DataList[i][this.PropertyActive] == true) {
                for (let j = 0; j < length; j++) {
                    if (this.DataList[j].IsSelected == true) {
                        this.DataList[j].IsSelected = false;
                        break;
                    }
                }
                this.CurrentNumber = i;
                this.DataList[i].IsSelected = true;
                this.TurnOff();
                this.onEnter.emit(this.DataList[i]);
                return;
            }
        }
    }

    ValidateDataList() {
        if (this.DataList == null || !this.IsValidateDataList) return;
        let length = this.DataList.length;
        let count = 0;
        for (let j = 0; j < length; j++) {
            if (this.DataList[j].IsSelected == true) {
                count++;
            }
        }
        if (this.CurrentNumber < this.DataList.length && count > 1) this.DataList[this.CurrentNumber].IsSelected = false;
        for (let j = 0; j < length; j++) {
            if (this.DataList[j].IsSelected == true) {
                this.CurrentNumber = j;
                return;
            }
        }
    }
}
