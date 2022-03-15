import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ItemFacturaCompraService } from '../service/item-factura-compra.service';

import { ItemFacturaCompraComponent } from './item-factura-compra.component';

describe('ItemFacturaCompra Management Component', () => {
  let comp: ItemFacturaCompraComponent;
  let fixture: ComponentFixture<ItemFacturaCompraComponent>;
  let service: ItemFacturaCompraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ItemFacturaCompraComponent],
    })
      .overrideTemplate(ItemFacturaCompraComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemFacturaCompraComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ItemFacturaCompraService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.itemFacturaCompras?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
