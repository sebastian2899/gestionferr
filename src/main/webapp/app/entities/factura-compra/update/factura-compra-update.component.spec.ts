import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FacturaCompraService } from '../service/factura-compra.service';
import { IFacturaCompra, FacturaCompra } from '../factura-compra.model';

import { FacturaCompraUpdateComponent } from './factura-compra-update.component';

describe('FacturaCompra Management Update Component', () => {
  let comp: FacturaCompraUpdateComponent;
  let fixture: ComponentFixture<FacturaCompraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let facturaCompraService: FacturaCompraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FacturaCompraUpdateComponent],
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
      .overrideTemplate(FacturaCompraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FacturaCompraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facturaCompraService = TestBed.inject(FacturaCompraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const facturaCompra: IFacturaCompra = { id: 456 };

      activatedRoute.data = of({ facturaCompra });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(facturaCompra));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FacturaCompra>>();
      const facturaCompra = { id: 123 };
      jest.spyOn(facturaCompraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facturaCompra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: facturaCompra }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(facturaCompraService.update).toHaveBeenCalledWith(facturaCompra);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FacturaCompra>>();
      const facturaCompra = new FacturaCompra();
      jest.spyOn(facturaCompraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facturaCompra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: facturaCompra }));
      saveSubject.complete();

      // THEN
      expect(facturaCompraService.create).toHaveBeenCalledWith(facturaCompra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FacturaCompra>>();
      const facturaCompra = { id: 123 };
      jest.spyOn(facturaCompraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facturaCompra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facturaCompraService.update).toHaveBeenCalledWith(facturaCompra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
