/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BillDetailComponent } from '../../../../../../main/webapp/app/entities/bill/bill-detail.component';
import { BillService } from '../../../../../../main/webapp/app/entities/bill/bill.service';
import { Bill } from '../../../../../../main/webapp/app/entities/bill/bill.model';

describe('Component Tests', () => {

    describe('Bill Management Detail Component', () => {
        let comp: BillDetailComponent;
        let fixture: ComponentFixture<BillDetailComponent>;
        let service: BillService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [BillDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BillService,
                    JhiEventManager
                ]
            }).overrideTemplate(BillDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BillDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BillService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Bill(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.bill).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
