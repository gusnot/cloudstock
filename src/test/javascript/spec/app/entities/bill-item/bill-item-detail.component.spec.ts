/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BillItemDetailComponent } from '../../../../../../main/webapp/app/entities/bill-item/bill-item-detail.component';
import { BillItemService } from '../../../../../../main/webapp/app/entities/bill-item/bill-item.service';
import { BillItem } from '../../../../../../main/webapp/app/entities/bill-item/bill-item.model';

describe('Component Tests', () => {

    describe('BillItem Management Detail Component', () => {
        let comp: BillItemDetailComponent;
        let fixture: ComponentFixture<BillItemDetailComponent>;
        let service: BillItemService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [BillItemDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BillItemService,
                    JhiEventManager
                ]
            }).overrideTemplate(BillItemDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BillItemDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BillItemService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new BillItem(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.billItem).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
