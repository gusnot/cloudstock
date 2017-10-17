/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SkuDetailComponent } from '../../../../../../main/webapp/app/entities/sku/sku-detail.component';
import { SkuService } from '../../../../../../main/webapp/app/entities/sku/sku.service';
import { Sku } from '../../../../../../main/webapp/app/entities/sku/sku.model';

describe('Component Tests', () => {

    describe('Sku Management Detail Component', () => {
        let comp: SkuDetailComponent;
        let fixture: ComponentFixture<SkuDetailComponent>;
        let service: SkuService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [SkuDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SkuService,
                    JhiEventManager
                ]
            }).overrideTemplate(SkuDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SkuDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SkuService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Sku(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.sku).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
