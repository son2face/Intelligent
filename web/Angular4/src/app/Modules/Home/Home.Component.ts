import {Component, OnInit} from '@angular/core';
import {ProblemService} from "../problem/problem.Service";
import {ShapeService} from "../shape/shape.Service";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    public problemEntity: any;

    // [[[0, 0], [100, 0] ], [[0, 0], [10, 17]]];
    constructor(private problemService: ProblemService, private shapeService: ShapeService,) {
        problemService.GetId("1").subscribe(problem => {
                this.problemEntity = problem;
                this.problemEntity.shapeEntities.forEach(shape => {
                    this.shapeService.GetId(shape.shapeId.toString()).subscribe(shapeEntity => {
                        shape.edgeEntities = shapeEntity.edgeEntities;
                        shape.convertedEdges = [];
                        shape.edgeEntities.forEach(edge => {
                            shape.convertedEdges.push([[edge.startX, edge.startY], [edge.endX, edge.endY]]);
                        })
                        console.log(shape.convertedEdges);
                    });
                })
            }
        );
    }

    ngOnInit() {
    }

}
