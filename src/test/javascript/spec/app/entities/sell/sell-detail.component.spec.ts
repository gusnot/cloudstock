/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SellDetailComponent } from '../../../../../../main/webapp/app/entities/sell/sell-detail.component';
import { SellService } from '../../../../../../main/webapp/app/entities/sell/sell.service';
import { Sell } from '../../../../../../main/webapp/app/entities/sell/sell.model';

describe('Component Tests', () => {

    describe('Sell Management Detail Component', () => {
        let comp: SellDetailComponent;
        let fixture: ComponentFixture<SellDetailComponent>;
        let service: SellService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [SellDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SellService,
                    JhiEventManager
                ]
            }).overrideTemplate(SellDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SellDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SellService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Sell(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.sell).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
