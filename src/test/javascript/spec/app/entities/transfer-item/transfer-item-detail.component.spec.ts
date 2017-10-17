/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TransferItemDetailComponent } from '../../../../../../main/webapp/app/entities/transfer-item/transfer-item-detail.component';
import { TransferItemService } from '../../../../../../main/webapp/app/entities/transfer-item/transfer-item.service';
import { TransferItem } from '../../../../../../main/webapp/app/entities/transfer-item/transfer-item.model';

describe('Component Tests', () => {

    describe('TransferItem Management Detail Component', () => {
        let comp: TransferItemDetailComponent;
        let fixture: ComponentFixture<TransferItemDetailComponent>;
        let service: TransferItemService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [TransferItemDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TransferItemService,
                    JhiEventManager
                ]
            }).overrideTemplate(TransferItemDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TransferItemDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransferItemService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TransferItem(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.transferItem).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
