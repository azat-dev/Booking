import LocalAuthDataRepositoryImpl from "../../../../data/auth/repositories/LocalAuthDataRepositoryImpl.ts";
import LocalAuthDataRepository from "../../../../domain/auth/interfaces/repositories/LocalAuthDataRepository.ts";
import singleton from "../../../../utils/singleton.ts";

class LocalAuthDataConfig {

    public localAuthDataRepository = singleton(
        (): LocalAuthDataRepository => {
            return new LocalAuthDataRepositoryImpl();
        }
    )
}

export default LocalAuthDataConfig;