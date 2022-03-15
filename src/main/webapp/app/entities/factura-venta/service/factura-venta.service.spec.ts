import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TipoFacturaEnum } from 'app/entities/enumerations/tipo-factura-enum.model';
import { IFacturaVenta, FacturaVenta } from '../factura-venta.model';

import { FacturaVentaService } from './factura-venta.service';

describe('FacturaVenta Service', () => {
  let service: FacturaVentaService;
  let httpMock: HttpTestingController;
  let elemDefault: IFacturaVenta;
  let expectedResult: IFacturaVenta | IFacturaVenta[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FacturaVentaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      numeroFactura: 'AAAAAAA',
      fechaCreacion: currentDate,
      infoCliente: 'AAAAAAA',
      valorFactura: 0,
      valorPagado: 0,
      valorDeuda: 0,
      tipoFactura: TipoFacturaEnum.CREDITO,
      idCliente: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a FacturaVenta', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.create(new FacturaVenta()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FacturaVenta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numeroFactura: 'BBBBBB',
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          infoCliente: 'BBBBBB',
          valorFactura: 1,
          valorPagado: 1,
          valorDeuda: 1,
          tipoFactura: 'BBBBBB',
          idCliente: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FacturaVenta', () => {
      const patchObject = Object.assign(
        {
          numeroFactura: 'BBBBBB',
          valorPagado: 1,
          valorDeuda: 1,
          tipoFactura: 'BBBBBB',
        },
        new FacturaVenta()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FacturaVenta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numeroFactura: 'BBBBBB',
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          infoCliente: 'BBBBBB',
          valorFactura: 1,
          valorPagado: 1,
          valorDeuda: 1,
          tipoFactura: 'BBBBBB',
          idCliente: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a FacturaVenta', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFacturaVentaToCollectionIfMissing', () => {
      it('should add a FacturaVenta to an empty array', () => {
        const facturaVenta: IFacturaVenta = { id: 123 };
        expectedResult = service.addFacturaVentaToCollectionIfMissing([], facturaVenta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(facturaVenta);
      });

      it('should not add a FacturaVenta to an array that contains it', () => {
        const facturaVenta: IFacturaVenta = { id: 123 };
        const facturaVentaCollection: IFacturaVenta[] = [
          {
            ...facturaVenta,
          },
          { id: 456 },
        ];
        expectedResult = service.addFacturaVentaToCollectionIfMissing(facturaVentaCollection, facturaVenta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FacturaVenta to an array that doesn't contain it", () => {
        const facturaVenta: IFacturaVenta = { id: 123 };
        const facturaVentaCollection: IFacturaVenta[] = [{ id: 456 }];
        expectedResult = service.addFacturaVentaToCollectionIfMissing(facturaVentaCollection, facturaVenta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facturaVenta);
      });

      it('should add only unique FacturaVenta to an array', () => {
        const facturaVentaArray: IFacturaVenta[] = [{ id: 123 }, { id: 456 }, { id: 19214 }];
        const facturaVentaCollection: IFacturaVenta[] = [{ id: 123 }];
        expectedResult = service.addFacturaVentaToCollectionIfMissing(facturaVentaCollection, ...facturaVentaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facturaVenta: IFacturaVenta = { id: 123 };
        const facturaVenta2: IFacturaVenta = { id: 456 };
        expectedResult = service.addFacturaVentaToCollectionIfMissing([], facturaVenta, facturaVenta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facturaVenta);
        expect(expectedResult).toContain(facturaVenta2);
      });

      it('should accept null and undefined values', () => {
        const facturaVenta: IFacturaVenta = { id: 123 };
        expectedResult = service.addFacturaVentaToCollectionIfMissing([], null, facturaVenta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(facturaVenta);
      });

      it('should return initial array if no FacturaVenta is added', () => {
        const facturaVentaCollection: IFacturaVenta[] = [{ id: 123 }];
        expectedResult = service.addFacturaVentaToCollectionIfMissing(facturaVentaCollection, undefined, null);
        expect(expectedResult).toEqual(facturaVentaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
