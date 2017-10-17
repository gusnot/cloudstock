/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TransactionItemDetailComponent } from '../../../../../../main/webapp/app/entities/transaction-item/transaction-item-detail.component';
import { TransactionItemService } from '../../../../../../main/webapp/app/entities/transaction-item/transaction-item.service';
import { TransactionItem } from '../../../../../../main/webapp/app/entities/transaction-item/transaction-item.model';

describe('Component Tests', () => {

    describe('TransactionItem Management Detail Component', () => {
        let comp: TransactionItemDetailComponent;
        let fixture: ComponentFixture<TransactionItemDetailComponent>;
        let service: TransactionItemService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [TransactionItemDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TransactionItemService,
                    JhiEventManager
                ]
            }).overrideTemplate(TransactionItemDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TransactionItemDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransactionItemService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TransactionItem(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.transactionItem).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
