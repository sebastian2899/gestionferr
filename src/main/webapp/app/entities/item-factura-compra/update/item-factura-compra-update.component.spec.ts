import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ItemFacturaCompraService } from '../service/item-factura-compra.service';
import { IItemFacturaCompra, ItemFacturaCompra } from '../item-factura-compra.model';

import { ItemFacturaCompraUpdateComponent } from './item-factura-compra-update.component';

describe('ItemFacturaCompra Management Update Component', () => {
  let comp: ItemFacturaCompraUpdateComponent;
  let fixture: ComponentFixture<ItemFacturaCompraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemFacturaCompraService: ItemFacturaCompraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ItemFacturaCompraUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ItemFacturaCompraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemFacturaCompraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemFacturaCompraService = TestBed.inject(ItemFacturaCompraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const itemFacturaCompra: IItemFacturaCompra = { id: 456 };

      activatedRoute.data = of({ itemFacturaCompra });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(itemFacturaCompra));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemFacturaCompra>>();
      const itemFacturaCompra = { id: 123 };
      jest.spyOn(itemFacturaCompraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemFacturaCompra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemFacturaCompra }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemFacturaCompraService.update).toHaveBeenCalledWith(itemFacturaCompra);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemFacturaCompra>>();
      const itemFacturaCompra = new ItemFacturaCompra();
      jest.spyOn(itemFacturaCompraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemFacturaCompra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemFacturaCompra }));
      saveSubject.complete();

      // THEN
      expect(itemFacturaCompraService.create).toHaveBeenCalledWith(itemFacturaCompra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemFacturaCompra>>();
      const itemFacturaCompra = { id: 123 };
      jest.spyOn(itemFacturaCompraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemFacturaCompra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemFacturaCompraService.update).toHaveBeenCalledWith(itemFacturaCompra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
