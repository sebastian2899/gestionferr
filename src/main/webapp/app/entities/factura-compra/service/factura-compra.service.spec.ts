import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TipoFacturaEnum } from 'app/entities/enumerations/tipo-factura-enum.model';
import { IFacturaCompra, FacturaCompra } from '../factura-compra.model';

import { FacturaCompraService } from './factura-compra.service';

describe('FacturaCompra Service', () => {
  let service: FacturaCompraService;
  let httpMock: HttpTestingController;
  let elemDefault: IFacturaCompra;
  let expectedResult: IFacturaCompra | IFacturaCompra[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FacturaCompraService);
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
      idProovedor: 0,
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

    it('should create a FacturaCompra', () => {
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

      service.create(new FacturaCompra()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FacturaCompra', () => {
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
          idProovedor: 1,
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

    it('should partial update a FacturaCompra', () => {
      const patchObject = Object.assign(
        {
          numeroFactura: 'BBBBBB',
          infoCliente: 'BBBBBB',
          valorFactura: 1,
          valorDeuda: 1,
          tipoFactura: 'BBBBBB',
          idProovedor: 1,
        },
        new FacturaCompra()
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

    it('should return a list of FacturaCompra', () => {
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
          idProovedor: 1,
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

    it('should delete a FacturaCompra', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFacturaCompraToCollectionIfMissing', () => {
      it('should add a FacturaCompra to an empty array', () => {
        const facturaCompra: IFacturaCompra = { id: 123 };
        expectedResult = service.addFacturaCompraToCollectionIfMissing([], facturaCompra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(facturaCompra);
      });

      it('should not add a FacturaCompra to an array that contains it', () => {
        const facturaCompra: IFacturaCompra = { id: 123 };
        const facturaCompraCollection: IFacturaCompra[] = [
          {
            ...facturaCompra,
          },
          { id: 456 },
        ];
        expectedResult = service.addFacturaCompraToCollectionIfMissing(facturaCompraCollection, facturaCompra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FacturaCompra to an array that doesn't contain it", () => {
        const facturaCompra: IFacturaCompra = { id: 123 };
        const facturaCompraCollection: IFacturaCompra[] = [{ id: 456 }];
        expectedResult = service.addFacturaCompraToCollectionIfMissing(facturaCompraCollection, facturaCompra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facturaCompra);
      });

      it('should add only unique FacturaCompra to an array', () => {
        const facturaCompraArray: IFacturaCompra[] = [{ id: 123 }, { id: 456 }, { id: 46907 }];
        const facturaCompraCollection: IFacturaCompra[] = [{ id: 123 }];
        expectedResult = service.addFacturaCompraToCollectionIfMissing(facturaCompraCollection, ...facturaCompraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facturaCompra: IFacturaCompra = { id: 123 };
        const facturaCompra2: IFacturaCompra = { id: 456 };
        expectedResult = service.addFacturaCompraToCollectionIfMissing([], facturaCompra, facturaCompra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facturaCompra);
        expect(expectedResult).toContain(facturaCompra2);
      });

      it('should accept null and undefined values', () => {
        const facturaCompra: IFacturaCompra = { id: 123 };
        expectedResult = service.addFacturaCompraToCollectionIfMissing([], null, facturaCompra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(facturaCompra);
      });

      it('should return initial array if no FacturaCompra is added', () => {
        const facturaCompraCollection: IFacturaCompra[] = [{ id: 123 }];
        expectedResult = service.addFacturaCompraToCollectionIfMissing(facturaCompraCollection, undefined, null);
        expect(expectedResult).toEqual(facturaCompraCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
