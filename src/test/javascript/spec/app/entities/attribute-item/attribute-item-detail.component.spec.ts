/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CloudstockTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AttributeItemDetailComponent } from '../../../../../../main/webapp/app/entities/attribute-item/attribute-item-detail.component';
import { AttributeItemService } from '../../../../../../main/webapp/app/entities/attribute-item/attribute-item.service';
import { AttributeItem } from '../../../../../../main/webapp/app/entities/attribute-item/attribute-item.model';

describe('Component Tests', () => {

    describe('AttributeItem Management Detail Component', () => {
        let comp: AttributeItemDetailComponent;
        let fixture: ComponentFixture<AttributeItemDetailComponent>;
        let service: AttributeItemService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CloudstockTestModule],
                declarations: [AttributeItemDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AttributeItemService,
                    JhiEventManager
                ]
            }).overrideTemplate(AttributeItemDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AttributeItemDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttributeItemService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AttributeItem(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.attributeItem).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
