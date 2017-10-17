/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BranchDetailComponent } from '../../../../../../main/webapp/app/entities/branch/branch-detail.component';
import { BranchService } from '../../../../../../main/webapp/app/entities/branch/branch.service';
import { Branch } from '../../../../../../main/webapp/app/entities/branch/branch.model';

describe('Component Tests', () => {

    describe('Branch Management Detail Component', () => {
        let comp: BranchDetailComponent;
        let fixture: ComponentFixture<BranchDetailComponent>;
        let service: BranchService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [BranchDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BranchService,
                    JhiEventManager
                ]
            }).overrideTemplate(BranchDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BranchDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BranchService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Branch(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.branch).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
