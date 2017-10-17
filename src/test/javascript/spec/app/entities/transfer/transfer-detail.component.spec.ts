/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TransferDetailComponent } from '../../../../../../main/webapp/app/entities/transfer/transfer-detail.component';
import { TransferService } from '../../../../../../main/webapp/app/entities/transfer/transfer.service';
import { Transfer } from '../../../../../../main/webapp/app/entities/transfer/transfer.model';

describe('Component Tests', () => {

    describe('Transfer Management Detail Component', () => {
        let comp: TransferDetailComponent;
        let fixture: ComponentFixture<TransferDetailComponent>;
        let service: TransferService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [TransferDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TransferService,
                    JhiEventManager
                ]
            }).overrideTemplate(TransferDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TransferDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransferService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Transfer(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.transfer).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
