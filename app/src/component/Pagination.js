import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';

const LEFT_PAGE = 'LEFT';
const RIGHT_PAGE = 'RIGHT';

const range = (from, to, step = 1) => {
    let i = from;
    const range = [];

    while (i <= to) {
        range.push(i);
        i += step;
    }

    return range;
};

class Pagination extends Component {

    constructor(props) {
        super(props);

        // const {totalRecords = null, pageLimit = 30, pageNeighbours = 0} = props;
        const {totalRecords = null, pageLimit = 30, pageNeighbours = 0, requiredPage = 1} = props;

        this.totalRecords = typeof totalRecords === 'number' ? totalRecords : 0;
        this.pageLimit = typeof pageLimit === 'number' ? pageLimit : 30;

        // pageNeighbours can be: 0, 1 or 2
        this.pageNeighbours = typeof pageNeighbours === 'number'
            ? Math.max(0, Math.min(pageNeighbours, 2))
            : 0;

        // alert("Pagination component here, requiredPage = " + this.requiredPage);
        this.requiredPage = typeof requiredPage === 'number' ? requiredPage : 1;

        this.totalPages = Math.ceil(this.totalRecords / this.pageLimit);
        if (this.totalPages === 0) {
            this.totalPages = 1;
        }


        // this.state = {currentPage: 1};
        this.state = {currentPage: requiredPage};
    }

    componentDidMount() {
        // this.gotoPage(1);
        this.gotoPage(this.requiredPage);

    }

    componentWillReceiveProps(nextProps, nextContent) {
        this.totalRecords = this.props.totalRecords;
        this.totalPages = Math.ceil(this.totalRecords / this.pageLimit);
        if ((this.state.currentPage > this.totalPages) && (this.totalRecords !== 0)) {
            this.setState({currentPages: this.totalPages});
            this.gotoPage(this.state.currentPage);
        }
        this.forceUpdate();
    }

    gotoPage = page => {
        const {onPageChanged = f => f} = this.props;
        let currentPage = Math.max(0, Math.min(page, this.totalPages));

        const paginationData = {
            currentPage,
            totalPages: this.totalPages,
            pageLimit: this.pageLimit,
            totalRecords: this.totalRecords
        };

        this.setState({currentPage}, () => onPageChanged(paginationData));
    };

    handleClick = page => evt => {
        evt.preventDefault();
        this.gotoPage(page);
    };

    handleMoveLeft = evt => {
        evt.preventDefault();
        this.gotoPage(this.state.currentPage - (this.pageNeighbours * 2) - 1);
    };

    handleMoveRight = evt => {
        evt.preventDefault();
        this.gotoPage(this.state.currentPage + (this.pageNeighbours * 2) + 1);
    };

    fetchPageNumbers = () => {

        let totalPages = this.totalPages;
        const currentPage = this.state.currentPage;
        const pageNeighbours = this.pageNeighbours;

        /**
         * totalNumbers: the total page numbers to show on the control
         * totalBlocks: totalNumbers + 2 to cover for the left(<) and right(>) controls
         */
        const totalNumbers = (this.pageNeighbours * 2) + 3;
        const totalBlocks = totalNumbers + 2;

        if (totalPages > totalBlocks) {

            const startPage = Math.max(2, currentPage - pageNeighbours);
            const endPage = Math.min(totalPages - 1, currentPage + pageNeighbours);

            let pages = range(startPage, endPage);

            /**
             * hasLeftSpill: has hidden pages to the left
             * hasRightSpill: has hidden pages to the right
             * spillOffset: number of hidden pages either to the left or to the right
             */
            const hasLeftSpill = startPage > 2;
            const hasRightSpill = (totalPages - endPage) > 1;
            const spillOffset = totalNumbers - (pages.length + 1);

            switch (true) {
                // handle: (1) < {5 6} [7] {8 9} (10)
                case (hasLeftSpill && !hasRightSpill): {
                    const extraPages = range(startPage - spillOffset, startPage - 1);
                    pages = [LEFT_PAGE, ...extraPages, ...pages];
                    break;
                }

                // handle: (1) {2 3} [4] {5 6} > (10)
                case (!hasLeftSpill && hasRightSpill): {
                    const extraPages = range(endPage + 1, endPage + spillOffset);
                    pages = [...pages, ...extraPages, RIGHT_PAGE];
                    break;
                }

                // handle: (1) < {4 5} [6] {7 8} > (10)
                case (hasLeftSpill && hasRightSpill):
                default: {
                    pages = [LEFT_PAGE, ...pages, RIGHT_PAGE];
                    break;
                }
            }

            return [1, ...pages, totalPages];

        }

        return range(1, totalPages);

    };

    render() {

        if (!this.totalRecords || this.totalPages === 1) return null;

        const {currentPage} = this.state;
        const pages = this.fetchPageNumbers();

        return (
            <Fragment>
                <nav aria-label="Cases Pagination">
                    <ul className="pagination align-content-md-center">
                        {pages.map((page, index) => {

                            if (page === LEFT_PAGE) return (
                                <li key={index} className="page-item">
                                    <a className="page-link" href="#" aria-label="Previous" onClick={this.handleMoveLeft}>
                                        <span aria-hidden="true">&laquo;</span>
                                        <span className="sr-only">Previous</span>
                                    </a>
                                </li>
                            );

                            if (page === RIGHT_PAGE) return (
                                <li key={index} className="page-item">
                                    <a className="page-link" href="#" aria-label="Next" onClick={this.handleMoveRight}>
                                        <span aria-hidden="true">&raquo;</span>
                                        <span className="sr-only">Next</span>
                                    </a>
                                </li>
                            );

                            return (
                                <li key={index} className={`page-item${ currentPage === page ? ' active' : ''}`}>
                                    <a className="page-link" href="#" onClick={this.handleClick(page)}>{ page }</a>
                                </li>
                            );})
                        }

                    </ul>
                </nav>
            </Fragment>
        );

    }
}

Pagination.propTypes = {
    totalRecords: PropTypes.number.isRequired,
    pageLimit: PropTypes.number,
    pageNeighbours: PropTypes.number,
    requiredPage: PropTypes.number,
    onPageChanged: PropTypes.func
};

export default Pagination;
