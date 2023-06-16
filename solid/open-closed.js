// Non using Open Closed Principle
// if (city === 'Cali') {
//     runCaliProcess();
// } else if (city === 'Lima') {
//     runLimaProcess();
// } else if (city === 'Buenos Aires') {
//     runBuenosAiresProcess();
// }

// Using Open Closed Principle
class CityProcess {
    run() {
        return 'Running...';
    }
}

class BuenosAiresProcess extends CityProcess {
    run() {
        return 'Running Buenos Aires process...';
    }
}

class CaliProcess extends CityProcess {
    run() {
        return 'Running Cali process...';
    }
}

class LimaProcess extends CityProcess {
    run() {
        return 'Running Lima process...';
    }
}

const main = () => {
    const cityProcess = new CityProcess();
    console.log(cityProcess.run());

    const caliProcess = new CaliProcess();
    console.log(caliProcess.run());
}

main();
