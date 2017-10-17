/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProductTypeDetailComponent } from '../../../../../../main/webapp/app/entities/product-type/product-type-detail.component';
import { ProductTypeService } from '../../../../../../main/webapp/app/entities/product-type/product-type.service';
import { ProductType } from '../../../../../../main/webapp/app/entities/product-type/product-type.model';

describe('Component Tests', () => {

    describe('ProductType Management Detail Component', () => {
        let comp: ProductTypeDetailComponent;
        let fixture: ComponentFixture<ProductTypeDetailComponent>;
        let service: ProductTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [ProductTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProductTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProductTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProductTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProductTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProductType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.productType).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
