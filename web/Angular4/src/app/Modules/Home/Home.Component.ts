import {Component, OnInit} from '@angular/core';
import {ProblemService} from "../problem/problem.Service";
import {ShapeService} from "../shape/shape.Service";
import {types} from "ngvas";
import Line = types.Line;

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    public problemEntity: any;
    public shapes: Array<Array<Line>> = [[[[0, 0], [100, 0]], [[0, 0], [10, 17]]], [[[0, 0], [100, 0]], [[0, 0], [10, 17]]]];
    public number = 0;
    // [[[0, 0], [100, 0] ], [[0, 0], [10, 17]]];
    constructor(private problemService: ProblemService, private shapeService: ShapeService,) {
        problemService.GetId("1").subscribe(problem => {
                this.problemEntity = problem;
                this.problemEntity.shapeEntities.forEach(shape => {
                    this.shapeService.GetId(shape.shapeId.toString()).subscribe(shapeEntity => {
                        shape.edgeEntities = shapeEntity.edgeEntities;
                        let t: Array<Line> =
                            [];
                        shape.edgeEntities.forEach(edge => {
                            t.push([[edge.startX, edge.startY], [edge.endX, edge.endY]]);
                        });
                        this.shapes.push(t);
                    });
                })
            }
        );
    }

    ngOnInit() {
    }

}
