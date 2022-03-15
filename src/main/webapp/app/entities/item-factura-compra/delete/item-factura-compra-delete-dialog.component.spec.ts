jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ItemFacturaCompraService } from '../service/item-factura-compra.service';

import { ItemFacturaCompraDeleteDialogComponent } from './item-factura-compra-delete-dialog.component';

describe('ItemFacturaCompra Management Delete Component', () => {
  let comp: ItemFacturaCompraDeleteDialogComponent;
  let fixture: ComponentFixture<ItemFacturaCompraDeleteDialogComponent>;
  let service: ItemFacturaCompraService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ItemFacturaCompraDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ItemFacturaCompraDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ItemFacturaCompraDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ItemFacturaCompraService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
