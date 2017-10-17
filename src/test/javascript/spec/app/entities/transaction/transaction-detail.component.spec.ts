/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TransactionDetailComponent } from '../../../../../../main/webapp/app/entities/transaction/transaction-detail.component';
import { TransactionService } from '../../../../../../main/webapp/app/entities/transaction/transaction.service';
import { Transaction } from '../../../../../../main/webapp/app/entities/transaction/transaction.model';

describe('Component Tests', () => {

    describe('Transaction Management Detail Component', () => {
        let comp: TransactionDetailComponent;
        let fixture: ComponentFixture<TransactionDetailComponent>;
        let service: TransactionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [TransactionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TransactionService,
                    JhiEventManager
                ]
            }).overrideTemplate(TransactionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TransactionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransactionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Transaction(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.transaction).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
