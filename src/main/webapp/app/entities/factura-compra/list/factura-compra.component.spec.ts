import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FacturaCompraService } from '../service/factura-compra.service';

import { FacturaCompraComponent } from './factura-compra.component';

describe('FacturaCompra Management Component', () => {
  let comp: FacturaCompraComponent;
  let fixture: ComponentFixture<FacturaCompraComponent>;
  let service: FacturaCompraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FacturaCompraComponent],
    })
      .overrideTemplate(FacturaCompraComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FacturaCompraComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FacturaCompraService);

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
    expect(comp.facturaCompras?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
